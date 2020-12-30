package com.turbid.explore.tools;


import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class AutoNewsCrawler extends BreadthCrawler {

    String title =null;
    String content=null;
    List<String> images=new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public AutoNewsCrawler(String crawlPath, boolean autoParse, String url) {
        super(crawlPath, autoParse);

        this.addSeed(url);//种子页面，起始页面

        //正则规则设置 寻找符合http://news.hfut.edu.cn/show-xxxxxxhtml的url
        this.addRegex("http://news.hfut.edu.cn/show-.*html");

        this.addRegex("-.*\\.(jpg|png|gif).*");

        //不要爬取包含 #的URL
        this.addRegex("-.*#.*");

        setThreads(50);//线程数

        getConf().setTopN(100);//设置每次迭代中爬取数量的上限
        getConf().setAutoDetectImg(true);
        //设置是否为断点爬取，如果设置为false，任务启动前会清空历史数据。
        //如果设置为true，会在已有crawlPath(构造函数的第一个参数)的基础上继
        //续爬取。对于耗时较长的任务，很可能需要中途中断爬虫，也有可能遇到
        //死机、断电等异常情况，使用断点爬取模式，可以保证爬虫不受这些因素
        //的影响，爬虫可以在人为中断、死机、断电等情况出现后，继续以前的任务
        //进行爬取。断点爬取默认为false*/
//        setResumable(true);
    }

    /*
        visit函数定制访问每个页面时所需进行的操作
    */
    @Override
    public void visit(Page page, CrawlDatums next) {

        String contentType = page.contentType();
        if(contentType==null){
            return;
        }else if (contentType.contains("html")) {
             title = page.select("h2#activity-name").first().text();//获取url标题
             content = page.select("div[id=js_content]>p").text();
             if(content==""){
                 content=page.select("div[id=js_content]>*>span").text();
             }
            //如果是网页，则抽取其中包含图片的URL，放入后续任务
            Elements imgs = page.select("img");
            for (Element img : imgs) {
                if(img.attributes().get("data-src")!=""&&!"".equals(img.attributes().get("data-src"))) {
                    images.add(img.attributes().get("data-src"));
                }
            }

        } else if (contentType.startsWith("image")) {

        }

    }

    public static void main(String[] args) throws Exception {

        AutoNewsCrawler crawler = new AutoNewsCrawler("crawl", true,"https://mp.weixin.qq.com/s?src=11&timestamp=1608881809&ver=2787&signature=--kpA1ASy5l1*VYGiy4VnCFb3WCCcXyHxY3CY2XTfXBVkexzpwO4qeI5Ube-jVCIZ4q8i0s9fHIWF5nB9ePI0IBf*THbQKZH5FZ32rf8mTbcW5nJ3e3hkhIMpSJiZfTl&new=1");
        crawler.start(4);//启动爬虫
        System.out.println(crawler.images.size());
    }

}
