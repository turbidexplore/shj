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
            //如果是网页，则抽取其中包含图片的URL，放入后续任务
            Elements imgs = page.select("div[id=js_content]>*>img");
            for (Element img : imgs) {
                images.add(img.attributes().get("data-src"));
            }

        } else if (contentType.startsWith("image")) {

        }

    }

    public static void main(String[] args) throws Exception {

        AutoNewsCrawler crawler = new AutoNewsCrawler("crawl", true,"https://mp.weixin.qq.com/s?__biz=MzA4MTE1ODQzOQ==&mid=2649924986&idx=1&sn=40091237ccf5dbb6010196b261fad891&chksm=879f4d32b0e8c424293ed344879fcd8e1f822e4f14f6b2707a064202aafa50f1602dcd209ffa&mpshare=1&scene=23&srcid=1207PN5Cmgvx1sMox0z9lNOh&sharer_sharetime=1607307489709&sharer_shareid=ea117f4cd18a273a7b4b167bcb120e39#rd");

        crawler.start(4);//启动爬虫

        System.out.println(crawler.images);
    }

}
