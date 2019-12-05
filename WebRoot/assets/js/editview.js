// editView

$(document).ready(function() {

	$('.summernote').summernote({
		lang : 'zh-CN',
		placeholder : '请填写内容...',
		tabsize : 2,
		height : 200,
		callbacks : {
			onImageUpload : function(files) {
				sendFile(files);
			}
		}
	});
});

function sendFile(file) {
	var data = new FormData();
	data.append("file", file[0]);
	$.ajax({
		data : data,
		type : "POST",
		url : base + "/i/updateimg",
		cache : false,
		contentType : false,
		processData : false,
		success : function(url) {
			$('.summernote').summernote('insertImage', data, 'img');
		}
	});
}