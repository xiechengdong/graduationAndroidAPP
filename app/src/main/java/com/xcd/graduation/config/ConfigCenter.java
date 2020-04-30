package com.xcd.graduation.config;

public class  ConfigCenter {

    //学生注册地址
    public static final String Stu_RegisterURl = "http://25x535820c.qicp.vip:39011/stu/register";
    //发送短信验证码地址
    public static final String GetCodeUrl = "http://25x535820c.qicp.vip:39011/stu/sendsms";
    //查询该学号是否被注册过地址
    public static final String IsRegister = "http://25x535820c.qicp.vip:39011/stu/findByStuNumber";
    //学生登录地址
    public static final String Stu_Login = "http://25x535820c.qicp.vip:39011/stu/stulogin";
    //教师登录地址
    public static final String Teacher_Login = "http://25x535820c.qicp.vip:39011/stu/teacherlogin";
    //重置密码验证手机
    public static final String FORGET_PASSWORD = "http://25x535820c.qicp.vip:39011/stu/finduserbyphone";
    //重置密码
    public static final String MODIFY_PASSWORD = "http://25x535820c.qicp.vip:39011/stu/modifypassword";
    //修改信息
    public static final String ADD_HEAD_IMAGE = "http://25x535820c.qicp.vip:39011/stu/modifyinfo";
    //修改手机
    public static final String CHNAGE_PHONE = "http://25x535820c.qicp.vip:39011/stu/changePhone";
    //修改面
    public static final String USER_MODIFY_PASSWORD = "http://25x535820c.qicp.vip:39011/stu/modifypassword";
    //查询所有书籍
    public static final String BOOK_FIND_ALL = "http://25x535820c.qicp.vip:39011/book/findall";
    //模糊查询数据
    public static final String SEARCH_CONTENT_BOOK = "http://25x535820c.qicp.vip:39011/book/searchbook";
    //根据教师id查询课程
    public static final String FIND_BOOKS_BY_TEACHER_ID = "http://25x535820c.qicp.vip:39011/book/findbyteacherid";
    //根据教师id和搜索内容查询课程
    public static final String FIND_BOOKS_BY_TEACHER_ID_AND_SEARCH_CONTENT = "http://25x535820c.qicp.vip:39011/book/searchbookbyteacherid";
    //教师添加课程
    public static final String TEACHER_ADD_BOOK = "http://25x535820c.qicp.vip:39011/book/addbook";
    //根据ID删除课程
    public static final String DELETE_BY_ID = "http://25x535820c.qicp.vip:39011/book/deleteById";
    //根据id查找课程内容
    public static final String FIND_CONTENT_BY_ID = "http://25x535820c.qicp.vip:39011/book/findcontentbyid";
    //学生添加课程
    public static final String STU_BOOK_ADD_BOOK = "http://25x535820c.qicp.vip:39011/stu_book/addbook";
    //学生查找课程
    public static final String STU_SHOW_MY_BOOK = "http://25x535820c.qicp.vip:39011/stu_book/findbystuid";
    //学生课程按输入内容模糊查找课程
    public static final String STU_SHOW_MY_BOOK_BY_CONDITION = "http://25x535820c.qicp.vip:39011/stu_book/findByCondition";
    //学生移除课程
    public static final String STU_REMOVE_BOOK = "http://25x535820c.qicp.vip:39011/stu_book/StudeleteByBookId";
    //根据学生id和书籍id查询是否添加过
    public static final String FIND_BOOK_BY_BOOKID_AND_STUID = "http://25x535820c.qicp.vip:39011/stu_book/findbybookidandstuid";
    //学生添加留言
    public static final String STU_ADD_MESSAGE = "http://25x535820c.qicp.vip:39011/message/addmessage";
    //根据书籍ID查询留言
    public static final String FIND_MESSAGE_BY_BOOKID = "http://25x535820c.qicp.vip:39011/message/findbybookid";
    //根据书籍id删除学生收藏
    public static final String DEL_BYBOOK_ID = "http://25x535820c.qicp.vip:39011/stu_book/delByBookId";
    //管理员登录
    public static final String ADMIN_LOGIN = "http://25x535820c.qicp.vip:39011/admin/adminlogin";
    //查找所有学生
    public static final String FIND_ALL_STUDENT = "http://25x535820c.qicp.vip:39011/stu/findAllStu";
    //根据学生ID删除学生
    public static final String DELSTU_BY_STUID = "http://25x535820c.qicp.vip:39011/stu/delByStuId";
    //根据管理员输入的内容模糊查询
    public static final String FIND_STU_BY_SEARCH_CONTENT = "http://25x535820c.qicp.vip:39011/stu/searchstu";
    //查询所有教师用户
    public static final String FIND_ALL_TEACHER = "http://25x535820c.qicp.vip:39011/stu/findAllTea";
    //根据管理员输入内容模糊查询教师用户
    public static final String FIND_TEA_BY_SEARCH_CONTENT = "http://25x535820c.qicp.vip:39011/stu/searchtea";
    //根据教师ID删除用户
    public static final String DEL_TEACHER_BY_ID = "http://25x535820c.qicp.vip:39011/stu/delByteaId";
    //添加广告
    public static final String ADMIN_ADD_GUANGGAO = "http://25x535820c.qicp.vip:39011/guanggao/addGuanggao";
    //查询所有广告
    public static final String ADMIN_FINDALL_GUANGGAO = "http://25x535820c.qicp.vip:39011/guanggao/findAll";
    //删除广告
    public static final String ADMIN_DEL_GUANGGAO = "http://25x535820c.qicp.vip:39011/guanggao/delbyId";
    //留言回复
    public static final String ADD_REPLY = "http://25x535820c.qicp.vip:39011/message_details/save";
    //根据父级id查询
    public static final String FIND_MESSAGEA_BY_PARENT_ID = "http://25x535820c.qicp.vip:39011/message_details/findByParentId";
    //根据热度降序查询课程
    public static final String SHOW_HOT_BOOKS = "http://25x535820c.qicp.vip:39011/book/findByfavorite";
    //系统消息
    public static final String FINDALL_SYSTEM_INFO = "http://25x535820c.qicp.vip:39011/systemInfo/findall";

    public static final String CHARSET = "utf-8";
    public static final String Server_ERR = "服务器异常,请求失败";
    public static final String SUCCESS = "OK";

    //验证用户注册信息合法性的语句
    public static final String UserNameIsNull = "用户名为空";
    public static final String UserNumberIsNull = "学号/编号为空";
    public static final String UserMajorIsNull = "专业/部门为空";
    public static final String UserPhoneIsNull = "手机号为空";
    public static final String UserPassword1IsNull = "密码为空";
    public static final String UserPassword2IsNull = "确认密码为空";
    public static final String UserCodesOrPhoneNull = "手机号格式不正确或验证码为空";
    public static final String Password1_not_Password2 = "密码不一致";
    public static final String Number_Error = "学生学号为10位数字，教师编号为5位数字";
    public static final String Phone_Error = "电话为11位数字";
    public static final String CodeIsNull = "验证码为空";

    //10位数或11位数正则表达式
    public static final String regex1 = "\\d{10}";
    public static final String regex2 = "\\d{11}";
    public static final String regex3 = "\\d{5}";

    //验证用户修改密码输入的合法性
    public static final String OldPasswordIsNull = "旧密码为空";
    public static final String NewPasswordIsNull = "新密码为空";
    public static final String NewPassword1IsNotNewPassword2 = "新密码和确认密码不相同";
    public static final String OldPasswordISNewPassword = "旧密码和新密码不能相同";
}
