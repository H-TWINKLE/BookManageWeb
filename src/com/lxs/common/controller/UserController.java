package com.lxs.common.controller;

import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.lxs.common.base.BaseController;
import com.lxs.common.interceptor.GobalInterceptor;
import com.lxs.common.model.Mail;
import com.lxs.common.model.Tip;
import com.lxs.common.model.User;
import com.lxs.common.plugins.MailPlugin;
import com.lxs.common.service.AdminService;
import com.lxs.common.utils.Constant;
import com.lxs.common.utils.Utils;
import com.lxs.common.vaildator.GobalValidator;

public class UserController extends BaseController {

	@Inject
	AdminService aService;

	@Inject
	MailPlugin mPlugin;

	public void index() {

		setTitle("雪霜文学管理系统");

		setAttr("count", aService.getAllCount());

		setAttr("list", aService.getAllTip(getUser()));

		render("index.html");

	}

	public void deletetip(Integer id) {

		vailId(id);

		if (new Tip().setTipId(id).setTipView(0).update()) {
			setTipMsg("删除成功！");
		} else {
			setTipMsg("删除失败！");
		}

		forwardAction("/admin");

	}

	public void user(Integer pages) {

		pages = getDefaultPages(pages);

		setAttr("list", aService.getPageUser(pages));

		setTitle("用户管理");

		render("user.html");

	}

	@ActionKey("/getimg")
	public void getimg(Integer id) {

		if (id == null) {

			User u = getUser();

			if (u != null) {
				redirect("/upload/temp/" + u.getUserImg());
				return;
			} else {
				redirect("/admin/assets/img/ui-sam.jpg");
				return;
			}

		} else {
			User u = aService.getUserById(id);

			if (u != null && u.getUserImg() != null) {
				redirect("/upload/temp/" + u.getUserImg());
				return;
			} else {
				redirect("/admin/assets/img/ui-sam.jpg");
			}

		}

	}

	public void userexam(Integer pages) {

		pages = getDefaultPages(pages);

		setPages(pages);

		setTitle("用户审核");

		setAttr("list", aService.getUserListByExam(pages));

		render("/admin/examuser.html");
	}

	public void userexamend(Integer id, String words) {

		vailId(id);

		User c = aService.getUserById(id);

		vailT(c);

		if (StrKit.notBlank(words)) {

			Tip t = new Tip();

			t.setTipContent(words);
			t.setTipTitle(c.getUserAccount() + ":  审核不通过");
			t.setTipContent(words);
			t.setTipUser(c.getUserId());
			t.setTipAdmin(((User) getUser()).getUserId());
			t.setTipView(1);
			t.setTipDates(new Date());
			t.save();

			c.setUserAuth(Constant.ASREADER);

		} else {
			switch (c.getUserAuth()) {
			case Constant.APPLYASADMIN:
				c.setUserAuth(Constant.ASADMIN);
				break;

			case Constant.APPLYASAUTHOR:
				c.setUserAuth(Constant.ASAUTHOR);
				break;
			}
		}

		c.setUserApply(0);

		c.update();

		setTipMsg("操作成功！");

		forwardAction("/admin/user");

	}

	public void deleteuser(Integer id) {
		vailId(id);

		if (aService.delete(new User().setUserId(id))) {
			setTipMsg("删除用户成功！");
		} else {
			setTipMsg("删除用户失败！");
		}

		forwardAction("/admin/user");

	}

	public void modifyuser(Integer id) {

		vailId(id);

		User u = aService.getUserById(id);

		vailT(u);

		setTitle("用户：" + u.getUserNickname());

		setAttr("user", u);

		render("modifyuser.html");

	}

	public void tomodify() {

		Integer id = getParaToInt("user.user_id");

		vailId(id);

		User u = getModel(User.class);

		vailT(u);

		String img = getPara("cronimg");

		if (StrKit.notBlank(img)) {

			String name = Utils.INSTANCE.getFilePath(u.getUserId());

			String path = PathKit.getWebRootPath() + "/upload/temp/" + name;

			if (Utils.INSTANCE.base64ToImage(img, path)) {
				u.setUserImg(name);
			}

		}

		User user = aService.updateUserModify(u);

		if (user != null) {
			if (user.getUserId() == getUser().getUserId()) {
				setUser(user);
			}
			setTipMsg("修改成功！");
		} else {
			setTipMsg("修改失败！");
		}

		forwardAction("/admin/user");

	}

	public void modifyuserauth(Integer id, Integer auth) {

		vailId(auth);

		vailId(id);

		if (aService.modifyUserAuthById(id, auth)) {
			setTipMsg("修改权限成功！");
			User u = getUser();
			u.setUserAuth(auth);
			setUser(u);
		} else {
			setTipMsg("修改权限失败！");
		}

		forwardAction("/admin/user");

	}

	@Clear(GobalInterceptor.class)
	@ActionKey("/register")
	public void register() {

		setTitle("注册");
		render("register.html");

	}

	@Clear(GobalInterceptor.class)
	@ActionKey("/veriemail")
	public void veriemail(String code) {

		vailT(code);

		Mail mail = aService.vailMail(code);

		User u;

		if (mail != null) {

			if (aService.checkEmail(mail.getMailAccount()) != null) {
				setTipMsg("该邮箱已经注册，请登录！");
				forwardAction("/");
				return;
			}

			u = aService.saveUser(mail);

			if (u != null) {

				setTipMsg("验证成功，请登录！");
			}

			forwardAction("/");

		}

	}

	@Clear(GobalInterceptor.class)
	@ActionKey("/modifypass")
	public void modifypass(String code) {

		vailT(code);

		User u = aService.vailUserCode(code);

		if (u != null) {

			setAttr("email", u.getUserAccount());
			setAttr("code", code);

			render("modifypass.html");

		} else {
			redirect("/");

		}

	}

	@Clear(GobalInterceptor.class)
	@Before(GobalValidator.class)
	@ActionKey("/tomodifypass")
	public void tomodifypass(String pass1, String pass2, String email) {

		User u = aService.checkEmail(email);

		if (u != null) {

			if (aService.updateUserPass(pass2, email)) {

				setTipMsg("密码修改成功！");
			} else {
				setTipMsg("密码修改失败！");
			}
			forwardAction("/index");

		} else {
			redirect("/");

		}

	}

	@Clear(GobalInterceptor.class)
	@ActionKey("/getpass")
	public void getpass(String email) {

		vailT(email);

		User u = aService.checkEmail(email);

		if (u == null) {
			setTipMsg("该用户尚未注册！");
			forwardAction("/register");
			return;
		}

		boolean flag = false;

		String code = StrKit.getRandomUUID();

		String url = Utils.INSTANCE.appendVefiUrl("modifypass", "book", getRequest().getServerPort(), code);

		try {
			mPlugin.sendMail(u.getUserAccount(), Utils.INSTANCE.sendEmilInText(u.getUserAccount(), url), "修改密码 book");

			flag = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (flag) {
			setTipMsg("验证信息已经发送到您的邮箱，请查收！");
			u.setUserCode(code);
			u.update();

		} else {

			setTipMsg("内部异常，请稍后再试！");
		}

		forwardAction("/register");

	}

	@Clear(GobalInterceptor.class)
	@Before(GobalValidator.class)
	@ActionKey("/toregister")
	public void toregister() {

		Mail mail = getModel(Mail.class);

		vailT(mail);

		User u = aService.checkEmail(mail.getMailAccount());

		if (u != null) {
			setTipMsg("该用户已经注册，请登录！");
			forwardAction("/register");
			return;
		}

		boolean flag = false;

		String code = StrKit.getRandomUUID();

		String url = Utils.INSTANCE.appendVefiUrl("veriemail", "book", getRequest().getServerPort(), code);

		try {
			mPlugin.sendMail(mail.getMailAccount(), Utils.INSTANCE.sendEmilInText(mail.getMailAccount(), url),
					"注册 book");

			flag = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (flag) {
			mail.setMailCode(StrKit.getRandomUUID());
			mail.setMailDates(new Date());
			mail.setMailCode(code);
			mail.save();
			setTipMsg("已经成功发送验证信息到您的邮箱，请前往查收！");

		} else {
			setTipMsg("内部异常，请稍后再试！");
		}

		forwardAction("/register");

	}

}
