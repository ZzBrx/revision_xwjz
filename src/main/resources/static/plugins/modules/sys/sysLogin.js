/*!
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * 
 * @author ThinkGem
 * @version 2017-4-18
 */
/*$("#username, #password").on("focus blur", function() {
	setTimeout(function() {
		var b = $(this).css("borderColor");
		if (b != "") {
			$(this).prev().css("color", b)
		}
	}, 100)
}).blur();*/
$("#loginForm").validate({
			submitHandler : function(c) {
				var username = $("#username").val(); 
				var password = $("#password").val(); 
				js.ajaxSubmitForm($(c), function(data, status, xhr) {
					
					if (data.result == "false" && data.message.length > 0) {
						js.showMessage(f.message)
					} else {
						js.loading("登录验证成功，正在进入...");
						location = "/index"
					}
				}, "json", true, "正在验证登录，请稍后...");
				$("#username").val(username);
				$("#password").val(password).select().focus();
			}
		});
