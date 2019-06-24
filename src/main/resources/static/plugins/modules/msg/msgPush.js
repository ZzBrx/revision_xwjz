/*!
 * 消息推送实现
 * @author ThinkGem
 */
$(function() {
	var e = true;
	if ("onfocusin" in document) {
		document.onfocusin = function() {
			e = true
		};
		document.onfocusout = function() {
			e = false
		}
	} else {
		window.onfocus = function() {
			e = true
		};
		window.onblur = function() {
			e = false
		}
	}
	var c = 0;
	var d = false;
	var b = document.title;
	var a = function() {
		if (e) {
			document.title = b;
			d = false;
			return
		}
		d = true;
		c++;
		if (c == 3) {
			c = 1
		}
		if (c == 1) {
			document.title = "【新消息】" + b
		}
		if (c == 2) {
			document.title = "【　　　】" + b
		}
		setTimeout("flashTitle()", 500)
	};
	function f() {
		if (!d) {
			a()
		}
	}
});