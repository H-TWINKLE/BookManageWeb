// 信息提示

if (tips != "") {
	setTimeout("shows()", 1000);
}

function shows() {
	toastr["warning"](tips)
}

function toread(id) {
	window.location.href = base + '/detail?id=' + id;
}

function openlogin() {

	document.getElementById("login").click();
}

function fileselect() {
	document.getElementById("file").click();
}

// 裁剪边框

var image = document.getElementById('img');

var cropper, canvas;

$('#file').change(function(e) {
	var file;
	var files = e.target.files;
	if (files && files.length > 0) {

		if (cropper != null) {
			cropper.destroy();
		}

		file = URL.createObjectURL(files[0]);
		$('#img').attr({
			'src' : file
		})
	}
	;

	cropper = new Cropper(image, {
		aspectRatio : 1,
		viewMode : 1,
		background : false, // 是否显示网格背景
		zoomable : false, // 是否允许放大图像
		guides : false, // 是否显示裁剪框虚线
		crop : function(event) { // 剪裁框发生变化执行的函数。
			canvas = cropper.getCroppedCanvas({ // 使用canvas绘制一个宽和高200的图片
				width : 200,
				height : 200,
			});

			$('#imgs').attr("src", canvas.toDataURL("image/png", 0.3)) // 使用canvas
			// toDataURL方法把图片转换为base64格式

			$('#cronimg').attr("value", canvas.toDataURL("image/png", 0.3))

			// var file = dataURLtoBlob($('#imgs').attr("src"));
			// //将base64格式图片转换为文件形式

		}
	});
})

// 富文本框

$(document).ready(function() {

	$('.summernote').summernote({
		lang : 'zh-CN',
		placeholder : '请填写内容...',
		tabsize : 2,
		height : 300,
		callbacks : {
			onImageUpload : function(file) {
				sendFile(file);

			}
		}
	});
});

// 上传图片

function sendFile(file) {
	var data = new FormData();
	data.append("file", file[0]);

	$.ajax({
		data : data,
		type : "POST",
		url : base + "/uploadimg",
		cache : false,
		contentType : false,
		processData : false,
		success : function(url) {
			$('.summernote').summernote('insertImage', url, 'img');
		}

	});
}

// 富文本框值判断

function check() {
	if ($('.summernote').summernote('isEmpty')) {
		return false;
	}
}

// modifyuserauth

function auth(id, auth) {
	window.location.href = base + "/admin/modifyuserauth?id=" + id + "&auth="
			+ auth;
}
