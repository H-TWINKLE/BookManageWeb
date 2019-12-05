package com.lxs.common.plugins;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lxs.common.model.Chapter;

public class SpiderPlugin {

	public List<Chapter> getAllChapterByOneBook(String url, Integer book_id, Integer user_id) {

		Document doc = null;
		try {
			doc = Jsoup.connect(url).timeout(5000).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (doc == null)
			return null;

		List<Chapter> list = new ArrayList<>();

		Chapter chapter;

		Elements ele = doc.getElementsByClass("excerpts").first().getElementsByTag("article");

		for (Element e : ele) {

			chapter = new Chapter();
			chapter.setChapterTypes(1);
			chapter.setChapterName(e.text());
			chapter.setChapterBook(book_id);
			chapter.setChapterAuthor(user_id);
			chapter.setChapterView(1);
			chapter.setChapterAuto(e.getElementsByTag("a").first().attr("href"));

			list.add(chapter);

		}

		return list;

	}

	public Chapter getAllChapterByOneBookDetail(Chapter chapter) {

		Document doc = null;
		try {
			doc = Jsoup.connect(chapter.getChapterAuto()).timeout(5000).get();
		} catch (IOException e) {
			return null;
		}

		if (doc == null)
			return null;

		chapter.setChapterContent(doc.getElementsByClass("article-content").first().html());
		chapter.setChapterDates(new Date());

		chapter.save();

		return chapter;

	}

}
