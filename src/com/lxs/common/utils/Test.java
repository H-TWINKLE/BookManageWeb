package com.lxs.common.utils;

import java.util.List;

import com.lxs.common.model.Chapter;
import com.lxs.common.plugins.SpiderPlugin;

public class Test {

	public static void main(String[] args) {

		Utils.INSTANCE.pluginStart();

		SpiderPlugin s = new SpiderPlugin();

		for (int x = 1; x <= 1; x++) {

			List<Chapter> list = s.getAllChapterByOneBook("http://www.daomubiji.com/dao-mu-bi-ji-" + String.valueOf(x),
					4 + x, 110);

			if (list != null) {
				for (Chapter c : list) {

					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						// TODO: handle exception
					}

					Utils.INSTANCE.addRunnableToPool(() -> {

						System.out.println(c.getChapterName() + ": ---正在爬取");

						s.getAllChapterByOneBookDetail(c);												
						
					});

				}

			}

			System.out.println("--------------------第" + x + "本书 ------ 输出结束---------------");

		}

	}

}
