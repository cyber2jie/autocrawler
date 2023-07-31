package io.jt.autocrawler.runner;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.RegexPool;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import io.jt.autocrawler.config.HttpConfig;
import io.jt.autocrawler.constants.ConfigConstants;
import io.jt.autocrawler.env.Environment;
import io.jt.autocrawler.function.SupplierConsumerWrapper;
import io.jt.autocrawler.http.Http;
import io.jt.autocrawler.model.Request;
import io.jt.autocrawler.parser.HttpConfigParser;
import io.jt.autocrawler.parser.model.InParsedData;
import io.jt.autocrawler.parser.model.OutParsedData;
import io.jt.autocrawler.progress.Progress;
import io.jt.autocrawler.progress.ProgressEventHandler;
import io.jt.autocrawler.util.AsyncUtil;
import io.jt.autocrawler.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HttpRunner implements Runner {
    private final Logger logger = LoggerFactory.getLogger(HttpRunner.class);

    @Override
    public void run(String conf) {
        String confStr = FileUtil.readUtf8String(conf);
        HttpConfig httpConfig = JSONUtil.parse(confStr, HttpConfig.class);
        logger.debug("加载配置文件{}成功", conf);
        if (httpConfig.getIn() != null &&
                httpConfig.getOut() != null
        ) {

            Environment.put(httpConfig);
            HttpConfigParser.HttpConfigParsedData httpConfigParsedData = new HttpConfigParser(httpConfig).parse();
            InParsedData inParsedData = httpConfigParsedData.getIn();
            OutParsedData outParsedData = httpConfigParsedData.getOut();

            int threads = httpConfig.getThreads();

            if (threads <= 0) threads = ConfigConstants.DEFAULT_THREADS;


            int page = httpConfig.getPage();

            if (page <= 0) page = ConfigConstants.DEFAULT_HTTP_PAGE;

            ExecutorService executor = ThreadUtil.newExecutor(threads);

            List<String> validUrls = inParsedData.getUrls().stream().
                    filter(this::isValidUrl).collect(Collectors.toList());

            if (logger.isDebugEnabled()) {
                logger.debug("任务请求url数 {}", validUrls.size());
            }

            Progress progress = Progress.create(validUrls.size());
            progress.registerEvent(new ProgressEventHandler() {
                @Override
                public void onFailed(Progress pg) {
                }

                @Override
                public void onComplete(Progress pg) {
                }

                @Override
                public void onProgress(Progress pg) {
                    logger.info("任务完成进度{}", pg.getCompleteRatio());
                }
            });

            ListUtil.page(validUrls,
                    page, (urls) -> {

                        SupplierConsumerWrapper<String, Object>[] suppliers = new SupplierConsumerWrapper[urls.size()];

                        for (int i = 0; i < urls.size(); i++) {

                            suppliers[i] = new SupplierConsumerWrapper<String, Object>(urls.get(i), (u) -> {

                                try {
                                    if (logger.isDebugEnabled()) {
                                        logger.debug("开始请求{}", u);
                                    }

                                    Http.HttpBuilder httpBuilder = Http.get(u);
                                    if (httpConfig.getHeaders() != null) {
                                        for (Object o : httpConfig.getHeaders().entrySet()) {
                                            try {
                                                Map.Entry entry = (Map.Entry) o;
                                                String h = Convert.toStr(entry.getKey(), "");
                                                String v = Convert.toStr(entry.getValue(), "");
                                                if (StrUtil.isNotBlank(h)) httpBuilder.header(h, v);
                                            } catch (Exception e) {
                                                logger.error("添加header错误,{}", e.getMessage());
                                            }
                                        }
                                    }

                                    Http.HttpBuilder.HttpClient client = httpBuilder.build();

                                    Http.HttpBuilder.Response response = client.response();

                                    Request request = new Request();
                                    request.setUrl(u);
                                    request.setResponse(response);

                                    outParsedData.getConsumer().consume(request, httpConfig.getOut(), response.getContent());
                                    progress.complete();
                                } catch (Exception e) {
                                    logger.error("获取{}内容错误，{}", u, e.getMessage());
                                    progress.fail();
                                }

                            });
                        }

                        AsyncUtil.asyncSupplyAll(executor, suppliers);

                    });


            executor.shutdown();

        }
        logger.debug("执行任务完成");
    }

    private Pattern pattern = Pattern.compile(RegexPool.URL_HTTP);

    public boolean isValidUrl(String url) {
        return pattern.matcher(url).matches();
    }
}
