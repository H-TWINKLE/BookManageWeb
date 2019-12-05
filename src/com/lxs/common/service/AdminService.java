package com.lxs.common.service;

import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.lxs.common.base.BaseService;
import com.lxs.common.model.Book;
import com.lxs.common.model.Chapter;
import com.lxs.common.model.Comment;
import com.lxs.common.model.Mail;
import com.lxs.common.model.Tip;
import com.lxs.common.model.Types;
import com.lxs.common.model.User;
import com.lxs.common.utils.Constant;

public class AdminService implements BaseService {

	public List<Types> getAllTypes() {
		return Types.dao.find("select * from types order by types_dates desc");
	}

	public User checkEmail(String email) {
		return User.dao.findFirst("select * from user where user_account=?", email);
	}

	public User vailUserCode(String code) {
		return User.dao.findFirst("select * from user where user_code=?", code);
	}

	public boolean modifyUserAuthById(Integer id, Integer auth) {

		return new User().setUserId(id).setUserAuth(auth).update();

	}

	public Chapter getChapterById(Integer id) {
		return Chapter.dao.findById(id);
	}

	public Chapter getChapterByIdAnd(Integer id) {
		return Chapter.dao.findFirst(
				"select * from chapter left join book on chapter_book=book.book_id "
						+ "left join user on chapter_author=user.user_id where chapter_id=? order by chapter_dates",
				id);
	}

	public Book getBookById(Integer id) {
		return Book.dao.findById(id);
	}

	public Book getBooksByIdAnd(Integer id) {
		return Book.dao.findFirst("select * from book left join types on book_types=types.types_id "
				+ "left join user on book_user=user.user_id where book_id=? order by book_dates desc", id);
	}

	public Types getTypesById(Integer id) {
		return Types.dao.findById(id);
	}

	public User updateUserModify(User u) {
		if (u.update()) {
			return getUserById(u.getUserId());
		}
		return null;
	}

	public User getUserById(Integer id) {
		return User.dao.findById(id);
	}

	public Mail vailMail(String code) {
		return Mail.dao.findFirst("select * from mail where mail_code=?", code);
	}

	public boolean updateUserPass(String pass, String email) {
		return User.dao.findFirst("select * from user where user_account=?", email).setUserPass(pass)
				.setUserDates(new Date()).update();
	}

	public Page<User> getPageUser(int pages) {
		return User.dao.paginate(pages, 10, "select *", "from user order by user_dates desc");
	}

	public User saveUser(Mail mail) {

		if (mail == null)
			return null;

		User u = new User();

		u.setUserAccount(mail.getMailAccount());
		u.setUserPass(mail.getMailPass());
		u.setUserNickname(mail.getMailNickname());
		u.setUserDates(new Date());
		u.setUserAuth(Constant.ASREADER);
		u.save();

		return u;

	}

	@Override
	public <T extends Model<T>> boolean delete(T t) {
		return t.delete();
	}

	public Page<Book> getBookList(int pages) {
		return Book.dao.paginate(pages, 10, "select *",
				"from book left join types on book.book_types=types.types_id left join user on book_user=user.user_id order by book_dates desc");

	}
	
	public Page<Comment> geCommmentList(int pages) {
		return Comment.dao.paginate(pages, 10, "select *",
				"from comment left join book on book.book_id=comment.comment_book left join user on comment_user=user.user_id left join chapter on chapter.chapter_id=comment.comment_chapter order by comment_dates desc");

	}

	public Page<Book> getBookListByUser(int pages, User u) {
		return Book.dao.paginate(pages, 10, "select *",
				"from book left join types on book.book_types=types.types_id left join user on book_user=user.user_id where book_user=? order by book_dates desc",
				u.getUserId());

	}

	public List<Book> getBookAllListByUser(User u) {
		return Book.dao.find(
				"select * from book left join types on book.book_types=types.types_id left join user on book_user=user.user_id where book_user=? order by book_dates desc",
				u.getUserId());

	}

	public Page<Chapter> getChapterList(int pages) {
		return Chapter.dao.paginate(pages, 10, "select *",
				"from chapter left join book on chapter_book=book.book_id left join user on chapter_author=user.user_id order by chapter_dates desc");

	}

	public Page<Chapter> getChapterList(int pages, User user) {
		return Chapter.dao.paginate(pages, 10, "select *",
				"from chapter left join book on chapter_book=book.book_id left join user on chapter_author=user.user_id where chapter_author=? order by chapter_dates desc",
				user.getUserId());

	}

	public Page<Chapter> getChapterListByExam(int pages) {
		return Chapter.dao.paginate(pages, 10, "select *",
				"from chapter left join book on chapter_book=book.book_id left join user on chapter_author=user.user_id where chapter_view=0 order by chapter_dates desc");

	}

	public Page<User> getUserListByExam(int pages) {
		return User.dao.paginate(pages, 10, "select *", "from user where user_apply=1 order by user_dates desc");

	}

	public Page<Book> getBookListByExam(int pages) {
		return Book.dao.paginate(pages, 10, "select *",
				"from book left join user on book_user=user.user_id where book_view=0 order by book_dates desc");

	}

	public Page<Types> getTypesList(int pages) {

		return Types.dao.paginate(pages, 10, "select *", "from types order by types_hot desc");

	}

	@Override
	public <T extends Model<T>> boolean save(T t) {
		return t.save();
	}

	@Override
	public <T extends Model<T>> boolean update(T t) {
		return t.update();
	}

	public List<Record> getAllCountByAuthor(User u) {
		return Db.find(
				"select  count(*)   as   count  from `book` where book_user=? union all "
						+ "select  count(*)   as   count  from `chapter` WHERE chapter_author=? union all "
						+ "select count(*) as   count   from book WHERE book_view=0 and book_user=? union all "
						+ "select  count(*)   as   count   from chapter WHERE chapter_view=0 and chapter_author=?",
				u.getUserId(), u.getUserId(), u.getUserId(), u.getUserId());
	}

	public List<Record> getAllCount() {
		return Db.find("select  count(*)   as   count  from `user` union all "
				+ "select  count(*)   as   count  from `user` WHERE user_apply=1 union all "
				+ "select count(*) as   count   from book WHERE book_view=0 union all "
				+ "select  count(*)   as   count   from chapter WHERE chapter_view=0");
	}

	public List<Tip> getAllTip(User u) {
		return Tip.dao.find(
				"select * from tip left join user on tip_admin=user.user_id where tip_user=? and tip_view=1 order by tip_dates desc",
				u.getUserId());
	}

	public List<Chapter> getAllChapterByBookId(Integer id) {
		return Chapter.dao.find("select * from chapter where chapter_book=? order by chapter_dates desc", id);
	}

	public Chapter getOneChapterByBookId(Integer id) {
		return Chapter.dao.findFirst("select * from chapter where chapter_book=?", id);
	}

	public List<Comment> getAllCommentByUserId(Integer id, Integer view) {
		return Comment.dao.find(
				"select * from comment left join book on book.book_id=comment_book left join user on user.user_id=comment_user where comment_user=? and comment_view=? order by comment_dates desc",
				id, view);
	}

	public List<Comment> getAllCommentByBookId(Integer id, Integer view) {
		return Comment.dao.find(
				"select * from comment left join book on book.book_id=comment_book left join user on user.user_id=comment_user where comment_book=? and comment_view=? order by comment_dates desc",
				id, view);
	}

	public List<Comment> getAllCommentByChapterId(Integer id, Integer view) {
		return Comment.dao.find(
				"select * from comment left join book on book.book_id=comment_book left join chapter on chapter.chapter_id=comment_chapter left join user on user.user_id=comment_user where comment_chapter=? and comment_view=? order by comment_dates desc",
				id, view);
	}

}
