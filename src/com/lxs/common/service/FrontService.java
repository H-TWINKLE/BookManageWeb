package com.lxs.common.service;

import java.util.List;

import com.lxs.common.model.Book;
import com.lxs.common.model.User;

public class FrontService {

	public User toLogin(String account, String pass) {

		return User.dao.findFirst("select * from user where user_account=? and user_pass=?", account, pass);

	}

	public List<Book> getBookLimitInt(Integer limit, Integer hot) {
		return Book.dao.find(
				"select * from book left join user on book_user=user_id where book_hot=? order by book_dates desc limit 0,"
						+ limit,
				hot);
	}

	public List<Book> getBookByTypes(Integer types) {
		return Book.dao.find(
				"select * from book left join user on book_user=user_id where book_types=? order by book_dates desc ",
				types);
	}

	public List<Book> getBookBySearch(String what) {

		what = "%" + what + "%";
		
		return Book.dao.find(
				"select * from book left join user on book_user=user_id where book_title like ? order by book_dates desc ",
				what);
	}

}
