#XiyouCourses

请求教务系统，爬取课表，，，跟进中

终于把教务系统搞定了，课表，个人信息，成绩，都可以拿到了

###1.GET请求验证码url http://222.24.19.201/CheckCode.aspx 拿到验证码图片和一个sessionID

###2.POST请求 http://222.24.19.201/default2.aspx 请求体加上9对键值对

1) ("__VIEWSTATE", "dDwtNTE2MjI4MTQ7Oz61IGQDPAm6cyppI+uTzQcI8sEH6Q==")

2) ("txtUserName", value1)， value1为学号

3) ("TextBox2", value2)， value2为密码

4) ("txtSecretCode", value3), value3为验证码

5) ("RadioButtonList1","学生")

6) ("Button1", "");

7) ("lbLanguage", "")

8) ("hidPdrs", "")

9) ("hidsc", "")

然后再把("Cookie", "sessionID")和("Referer", "http://222.24.19.201") 加到请求头中，同时禁止重定向(必须禁止，不然请求不到)，然后之前的sessionID就会生效，就可以进到主页了。

以后每次请求都要把这个设置到请求头里面。

拿到session之后就能够访问课表，成绩，个人信息等内容了，不过在这之前还得先进到主页，拿到每个人的姓名。

GET请求http://222.24.19.201/xs_main.aspx?xh=xxxxxxxx。

禁止重定向，其中xh后跟学号，再把Cookie作为键，之前拿到的sessionID作为值添加到请求头里面，就可以进到主页了，

然后解析html页面（Jsoup），就可以拿到姓名了。

如果请求课表页面，GET请求http://222.24.19.201/xskbcx.aspx?xh=04141052&xm=xxxxxx&gnmkdm=N121603。

xm后加上姓名，并且禁止重定向，同时给请求头加上两对键值对

1). （” Cookie”, sessionID）；

2). （”Referer”, 要请求的地址,也就是http://222.24.19.201/xskbcx.aspx?xh=04141052&xm=xxxxxx&gnmkdm=N121603）；

这样就可以进到课表页面了，然后解析就可以愉快的拿到课表了。
