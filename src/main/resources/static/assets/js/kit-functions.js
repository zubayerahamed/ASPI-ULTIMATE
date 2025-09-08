/**
 * Application basepath
 */
function getBasepath(){
	const basePath = $('a.basePath').attr('href').split('/')[1];
	const href = location.href.split('/');
	return basePath ? `${href[0]}//${href[2]}/${basePath}` : `${href[0]}//${href[2]}`;
}



function disableBtn(btnClasses){
	$.each(btnClasses, function(i, btn){
		$('.' + btn).attr('disabled', 'true');
	});
}

function hideBtn(btnClasses){
	$.each(btnClasses, function(i, btn){
		$('.' + btn).addClass('d-none');
	});
}

function showBtn(btnClasses){
	$.each(btnClasses, function(i, btn){
		$('.' + btn).removeClass('d-none');
	});
}

function enableBtn(btnClasses){
	$.each(btnClasses, function(i, btn){
		$('.' + btn).removeAttr('disabled');
	});
}

function showProgress(colorClass, title){
	$('#progress').show();

	if(title != undefined && title != null){
		$('.porogress-text').html(title);
	}

	if(colorClass != undefined && colorClass != null && colorClass != ''){
		$('#progress #progressBar').addClass(colorClass);
	}
}

function updateProgress(percentage, colorClass){
	if(percentage > 100) percentage = 100;
	$('#progress #progressBar').css('width', percentage + '%').text(percentage.toFixed(2) + '%');

	if(colorClass != undefined && colorClass != null && colorClass != ''){
		$('#progress #progressBar').addClass(colorClass);
	}
}

function hideProgress(restalso){
	$('#progress').hide();
	if(restalso != undefined && restalso != null && restalso == true){
		resetProgressBar();
	}
}

function resetProgressBar(){
	$('#progress #progressBar').css('width', '0%').text('0%');
	$('#progress #progressBar').removeClass('bg-success');
	$('#progress #progressBar').removeClass('bg-warning');
	$('#progress #progressBar').removeClass('bg-danger');
}



// Toaster message
function showMessage(type, message, timeout) {
	if (type == undefined || type == "") return;

	if(timeout == undefined || timeout == "") timeout = 2500;

	new Noty({
		text: message,
		type: type,
		timeout: timeout,
		layout: 'bottomRight',
		closeWith: ['button']
	}).show();
}




/**
 * Loading mask object
 * function1 : show  -- Show loading mask
 * function2 : hide  -- Hide loading mask
 */
var loadingMask2 = {
	show: function () {
		$("div#loadingmask2, div.loadingdots, div#loadingdots").removeClass("nodisplay");
	},
	hide: function () {
		$("div#loadingmask2, div.loadingdots, div#loadingdots").addClass("nodisplay");
	},
};


function sectionReloadAjaxReq(section, callback) {
	loadingMask2.show();
	$.ajax({
		url: getBasepath() + section.url,
		type: "GET",
		success: function (data) {
			loadingMask2.hide();
			$("." + section.id).html("");
			$("." + section.id).append(data);
			
			if(callback) callback();
		},
		error: function (jqXHR, status, errorThrown) {
			loadingMask2.hide();
			if (jqXHR.status === 401) {
				// Session is invalid, reload the url to go back to login page
				location.reload();
			} else {
				showMessage("error", jqXHR.responseJSON.message);
			}
		},
	});
}

function sectionReloadAjaxPostReq(section, data, callback) {
	loadingMask2.show();
	$.ajax({
		url: getBasepath() + section.url,
		type: "POST",
		data: data,
		success: function (data) {
			loadingMask2.hide();
			$("." + section.id).html("");
			$("." + section.id).append(data);

			if(callback) callback();
		},
		error: function (jqXHR, status, errorThrown) {
			loadingMask2.hide();
			if (jqXHR.status === 401) {
				// Session is invalid, reload the url to go back to login page
				location.reload();
			} else {
				showMessage("error", jqXHR.responseJSON.message);
			}
		},
	});
}

function sectionReloadAjaxDeleteReq(section, data, callbackFunction) {
	loadingMask2.show();
	$.ajax({
		url: getBasepath() + section.url,
		type: "DELETE",
		data: data,
		success: function (data) {
			loadingMask2.hide();
			$("." + section.id).html("");
			$("." + section.id).append(data);

			if(callbackFunction != undefined){
				callbackFunction();
			}
		},
		error: function (jqXHR, status, errorThrown) {
			loadingMask2.hide();
			if (jqXHR.status === 401) {
				// Session is invalid, reload the url to go back to login page
				location.reload();
			} else {
				showMessage("error", jqXHR.responseJSON.message);
			}
		},
	});
}

function submitMainForm(customurl, customform, callbackFunction, callbackFunctionIfSuccess, callbackFunctionIfError){
	if((customform == undefined || customform == null) && $('form#mainform').length < 1) return;

	var targettedForm = customform == undefined || customform == null ? $('form#mainform') : customform;
	if(!targettedForm.smkValidate()) return;

	var submitUrl = (customurl == undefined || customurl == null) ? targettedForm.attr('action') : customurl;
	var submitType = targettedForm.attr('method');
	var formData = $(targettedForm).serializeArray();
	var enctype = targettedForm.attr('enctype');
	if(enctype == 'multipart/form-data'){
		submitMultipartForm(submitUrl, submitType, targettedForm, false);
		return;
	}

	loadingMask2.show();
	$.ajax({
		url : submitUrl,
		type :submitType,
		data : formData,
		success : function(data, status, xhr) {
			loadingMask2.hide();

			// For file download
			if(data.fileDownload == true){
				if("application/octet-stream" == data.mediaType.type + '/' + data.mediaType.subtype){
					var blob = new Blob([data.file], { type: data.mediaType.type + '/' + data.mediaType.subtype });
					var url = window.URL.createObjectURL(blob);
					var a = document.createElement('a');
					a.href = url;
					a.download = data.fileName;
					document.body.appendChild(a);
					a.click();
					window.URL.revokeObjectURL(url);
					showMessage(data.status.toLowerCase(), data.message);
					return;
				}
			}

			if(data.status == 'SUCCESS'){
				if(data.displayMessage == true) showMessage(data.status.toLowerCase(), data.message);

				if(data.triggermodalurl){
					modalLoader(getBasepath() + data.triggermodalurl, data.modalid);
				} else {
					if(data.reloadsections != undefined && data.reloadsections.length > 0){
						$.each(data.reloadsections, function (ind, section) {
							if(section.postData.length > 0){
								var data = {};
								$.each(section.postData, function(pi, pdata){
									data[pdata.key] = pdata.value;
								})
								sectionReloadAjaxPostReq(section, data);
							} else {
								sectionReloadAjaxReq(section);
							}
						});
					} else if(data.reloadurl){
						doSectionReloadWithNewData(data);
					} else if(data.redirecturl){
						setTimeout(() => {
							window.location.replace(getBasepath() + data.redirecturl);
						}, 1000);
					}
				}
				
				if(callbackFunctionIfSuccess != undefined && callbackFunctionIfSuccess != null){
					callbackFunctionIfSuccess();
				}
			} else {
				if(data.displayErrorDetailModal){
					$('#errorDetailModal').modal('show');

					sectionReloadAjaxReq({
						id : data.reloadelementid,
						url : data.reloadurl,
					});
				}

				if(data.status) showMessage(data.status.toLowerCase(), data.message);

				if(callbackFunctionIfError != undefined && callbackFunctionIfError != null){
					callbackFunctionIfError();
				}
			}

			if(callbackFunction != undefined && callbackFunction != null){
				callbackFunction();
			}

			if(data.printReport){
				generateOnScreenReport(getBasepath() + data.printUrl, data.printParams);
			}
		}, 
		error : function(jqXHR, status, errorThrown){
			loadingMask2.hide();
			if (jqXHR.status === 401) {
				// Session is invalid, reload the url to go back to login page
				location.reload();
			} else {
				showMessage("error", jqXHR.responseJSON.message);
			}
		}
	});
}

function submitMultipartForm(submitUrl, submitType, targettedForm, frommodal){
	var files = $('#fileuploader').get(0).files;

	var formData = new FormData();
	if(files.length == 1){
		formData.append("file", files[0]);
	}
	for (var x = 0; x < files.length; x++) {
		formData.append("files[]", files[x]);
	}

	$.each($(targettedForm).serializeArray(), function(i, b){
		formData.append(b.name, b.value);
	})

	loadingMask2.show();
	$.ajax({
		url : submitUrl,
		type :submitType,
		data : formData,
		async: false,
		cache: false,
		processData: false,
		contentType: false,
		success : function(data) {
			loadingMask2.hide();

			if(data.status == 'SUCCESS'){
				if(data.displayMessage == true) showMessage(data.status.toLowerCase(), data.message);

				if(data.triggermodalurl){
					modalLoader(getBasepath() + data.triggermodalurl, data.modalid);
				} else {
					if(data.reloadsections != undefined && data.reloadsections.length > 0){
						$.each(data.reloadsections, function (ind, section) {
							if(section.postData.length > 0){
								var data = {};
								$.each(section.postData, function(pi, pdata){
									data[pdata.key] = pdata.value;
								})
								sectionReloadAjaxPostReq(section, data);
							} else {
								sectionReloadAjaxReq(section);
							}
						});
					} else if(data.reloadurl){
						doSectionReloadWithNewData(data);
					} else if(data.redirecturl){
						setTimeout(() => {
							window.location.replace(getBasepath() + data.redirecturl);
						}, 1000);
					}
				}
			} else {
				if(data.displayErrorDetailModal){
					$('#errorDetailModal').modal('show');

					sectionReloadAjaxReq({
						id : data.reloadelementid,
						url : data.reloadurl,
					});
				}

				showMessage(data.status.toLowerCase(), data.message);
			}

			if(data.printReport){
				generateOnScreenReport(getBasepath() + data.printUrl, data.printParams);
			}
		}, 
		error : function(jqXHR, status, errorThrown){
			loadingMask2.hide();
			if (jqXHR.status === 401) {
				// Session is invalid, reload the url to go back to login page
				location.reload();
			} else {
				showMessage("error", jqXHR.responseJSON.message);
			}
		}
	});
}


function deleteRequest(customurl, data, callbackFunction, callbackFunctionIfSuccess, callbackFunctionIfError){
	loadingMask2.show();
	$.ajax({
		url : customurl,
		type : 'DELETE',
		data : data,
		success : function(data) {
			loadingMask2.hide();
			if(data.status == 'SUCCESS'){
				if(data.displayMessage == true) showMessage(data.status.toLowerCase(), data.message);

				if(callbackFunctionIfSuccess != undefined && callbackFunctionIfSuccess != null){
					callbackFunctionIfSuccess();
				}

				if(data.triggermodalurl){
					modalLoader(getBasepath() + data.triggermodalurl, data.modalid);
				} else {
					if(data.reloadsections != undefined && data.reloadsections.length > 0){
						$.each(data.reloadsections, function (ind, section) {
							if(section.postData.length > 0){
								var data = {};
								$.each(section.postData, function(pi, pdata){
									data[pdata.key] = pdata.value;
								})
								sectionReloadAjaxPostReq(section, data);
							} else {
								sectionReloadAjaxReq(section);
							}
						});
					} else if(data.reloadurl){
						doSectionReloadWithNewData(data);
					} else if(data.redirecturl){
						setTimeout(() => {
							window.location.replace(getBasepath() + data.redirecturl);
						}, 1000);
					}
				}
			} else {
				if(data.displayErrorDetailModal){
					$('#errorDetailModal').modal('show');

					sectionReloadAjaxReq({
						id : data.reloadelementid,
						url : data.reloadurl,
					});
				}

				showMessage(data.status.toLowerCase(), data.message);
			}
		}, 
		error : function(jqXHR, status, errorThrown){
			loadingMask2.hide();
			if (jqXHR.status === 401) {
				// Session is invalid, reload the url to go back to login page
				location.reload();
			} else {
				showMessage("error", jqXHR.responseJSON.message);
			}
		}
	});
}



function actionPostRequest(customurl, data, timeout, callbackFunction, callbackFunctionIfSuccess, callbackFunctionIfError){
	loadingMask2.show();
	$.ajax({
		url : customurl,
		type : 'POST',
		data : data,
		success : function(data) {
			loadingMask2.hide();
			if(data.status == 'SUCCESS'){
				if(data.displayMessage == true) showMessage(data.status.toLowerCase(), data.message);

				if(callbackFunctionIfSuccess != undefined && callbackFunctionIfSuccess != null){
					callbackFunctionIfSuccess();
				}

				if(data.triggermodalurl){
					modalLoader(getBasepath() + data.triggermodalurl, data.modalid);
				} else {
					if(data.reloadsections != undefined && data.reloadsections.length > 0){
						$.each(data.reloadsections, function (ind, section) {
							if(section.postData.length > 0){
								var data = {};
								$.each(section.postData, function(pi, pdata){
									data[pdata.key] = pdata.value;
								})
								sectionReloadAjaxPostReq(section, data);
							} else {
								sectionReloadAjaxReq(section);
							}
						});
					} else if(data.reloadurl){
						doSectionReloadWithNewData(data);
					} else if(data.redirecturl){
						setTimeout(() => {
							window.location.replace(getBasepath() + data.redirecturl);
						}, 1000);
					}
				}
			} else {
				if(data.displayErrorDetailModal){
					$('#errorDetailModal').modal('show');

					sectionReloadAjaxReq({
						id : data.reloadelementid,
						url : data.reloadurl,
					});
				}

				if(timeout == undefined || timeout == null) timeout = 2000;
				showMessage(data.status.toLowerCase(), data.message, timeout);
			}
		}, 
		error : function(jqXHR, status, errorThrown){
			loadingMask2.hide();
			if (jqXHR.status === 401) {
				// Session is invalid, reload the url to go back to login page
				location.reload();
			} else {
				showMessage("error", jqXHR.responseJSON.message);
			}
		}
	});
}


function generateOnScreenReport(customurl, data, reportType){
	if(reportType == undefined || reportType == '') reportType = "PDF";

	loadingMask2.show();
	$.ajax({
		url : customurl,
		type : 'POST',
		data : data,
		success : function(data) {
			loadingMask2.hide();
			var arrrayBuffer = base64ToArrayBuffer(data);
			if("PDF" == reportType){
				var blob = new Blob([arrrayBuffer], {type: "application/pdf"});
				var link = window.URL.createObjectURL(blob);
				window.open(link,'', 'height=650,width=840');
			} else {
				var blob = new Blob([arrrayBuffer], {type: "application/octetstream"});
				var isIE = false || !!document.documentMode;
				if (isIE) {
					window.navigator.msSaveBlob(blob, reportName + ".xls");
				} else {
					var url = window.URL || window.webkitURL;
					link = url.createObjectURL(blob);
					var a = $("<a />");
					a.attr("download", reportName + ".xls");
					a.attr("href", link);
					$("body").append(a);
					a[0].click();
					$(a, "body").remove();
				}
			}
		}, 
		error : function(jqXHR, status, errorThrown){
			loadingMask2.hide();
			if (jqXHR.status === 401) {
				// Session is invalid, reload the url to go back to login page
				location.reload();
			} else {
				showMessage("error", jqXHR.responseJSON.message);
			}
		}
	});
}


function validateAndSubmitReportForm(customurl, customvalidateUrl){
	if($('form#reportform').length < 1) return;

	var targettedForm = $('form#reportform');
	if(!targettedForm.smkValidate()) return;

	var validateUrl = (customvalidateUrl != undefined) ? customvalidateUrl : $(targettedForm).data('validate-url');
	var submitType = targettedForm.attr('method');
	var formData = $(targettedForm).serializeArray();

	// check validation first
	loadingMask2.show();
	$.ajax({
		url : validateUrl,
		type : submitType,
		data : formData,
		success : function(data) {
			loadingMask2.hide();
			if(data.status == 'SUCCESS'){
				if(data.displayMessage == true) showMessage(data.status.toLowerCase(), data.message);
				submitReportForm(null, data.rparam);
			} else {
				showMessage(data.status.toLowerCase(), data.message);
			}
		}, 
		error : function(jqXHR, status, errorThrown){
			loadingMask2.hide();
			if (jqXHR.status === 401) {
				// Session is invalid, reload the url to go back to login page
				location.reload();
			} else {
				showMessage("error", jqXHR.responseJSON.message);
			}
		}
	});

}


/**
 * Submit Report form
 * @param customurl
 * @param rParam Replacable Param or Addition Param
 * @returns
 */
function submitReportForm(customurl, rParam){
	if($('form#reportform').length < 1) return;

	var targettedForm = $('form#reportform');
	if(!targettedForm.smkValidate()) return;

	var submitUrl = (customurl != undefined && customurl != null) ? customurl : targettedForm.attr('action');
	var submitType = targettedForm.attr('method');
	var formData = $(targettedForm).serializeArray();

	// New or updatable params should add here
	if(rParam != undefined && rParam != null){
		$.each(rParam, function(key, value) {
			var fieldExists = false;

			// Check if the field already exists in formData
			formData.forEach(function(field) {
				if (field.name === key) {
					field.value = value; // Update the existing field
					fieldExists = true;
				}
			});

			// If the field doesn't exist, add it
			if (!fieldExists) {
				formData.push({ name: key, value: value });
			}
		});
	}

	var reportType = $('#reportType').val();
	if(reportType == undefined || reportType == '') reportType = "PDF";
	var reportName = $('#reportName').val() != '' ? $('#reportName').val() : 'report';

	loadingMask2.show();
	$.ajax({
		url : submitUrl,
		type : submitType,
		data : formData,
		success : function(data) {
			loadingMask2.hide();
			doReportProcess(data);
		}, 
		error : function(jqXHR, status, errorThrown){
			loadingMask2.hide();
			if (jqXHR.status === 401) {
				// Session is invalid, reload the url to go back to login page
				location.reload();
			} else {
				showMessage("error", jqXHR.responseJSON.message);
			}
		}
	});
}



function doReportProcess(data2){
	loadingMask2.show();

	$.ajax({
		url : 'http://localhost:2025/report-server/report/generate',
		type : 'POST',
		contentType: "application/json; charset=utf-8",   // tell server it's JSON
		data: JSON.stringify(data2), 
		success : function(data) {
			//console.log(data);
			loadingMask2.hide();

			var arrrayBuffer = base64ToArrayBuffer(data);
			if("PDF" == data2.reportType){
				var blob = new Blob([arrrayBuffer], {type: "application/pdf"});
				var link = window.URL.createObjectURL(blob);
				window.open(link,'', 'height=650,width=840');
				/*var a = $("<a />");
					a.attr("download", data2.reportTitle + ".pdf");
					a.attr("href", link);
					$("body").append(a);
					a[0].click();
					$(a, "body").remove();*/
			} else {
				var blob = new Blob([arrrayBuffer], {type: "application/octetstream"});
				var isIE = false || !!document.documentMode;
				if (isIE) {
					window.navigator.msSaveBlob(blob, data2.reportTitle + ".xls");
				} else {
					var url = window.URL || window.webkitURL;
					link = url.createObjectURL(blob);
					var a = $("<a />");
					a.attr("download", data2.reportTitle + ".xls");
					a.attr("href", link);
					$("body").append(a);
					a[0].click();
					$(a, "body").remove();
				}
			}
		}, 
		error : function(jqXHR, status, errorThrown){
			loadingMask2.hide();
			if (jqXHR.status === 401) {
				// Session is invalid, reload the url to go back to login page
				location.reload();
			} else {
				//showMessage("error", jqXHR.responseJSON.message);
			}
		}
	});
	
}



/**
 * Convert Base64 string to array buffer
 * @param base64
 * @returns
 */
function base64ToArrayBuffer(base64) {
	var binaryString = window.atob(base64);
	var binaryLen = binaryString.length;
	var bytes = new Uint8Array(binaryLen);
	for (var i = 0; i < binaryLen; i++) {
		var ascii = binaryString.charCodeAt(i);
		bytes[i] = ascii;
	}
	return bytes;
}
