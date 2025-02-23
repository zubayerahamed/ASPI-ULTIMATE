var columnDefs = [];
var targetCounter = 0;
$.each(columns, function(index, el){
	if(el.suffix.includes(0)) {
		columnDefs.push({
			name : el.name,
			targets : targetCounter++,
		});
	} 
});

var dataRetreiver = [];
$.each(columns, function(index, el){
	if(el.suffix.includes(0)) {
		dataRetreiver.push({
			data : el.rendername,
			render : el.render,
			class : el.class
		});
	} 
});

var dt = $('.' + tablename).DataTable({
	"deferLoading": true,
	"processing" : true,
	"serverSide" : true,
	"order" : [orderarr],
	"columnDefs": columnDefs,
	"ajax": {
		"url": getBasepath() + "/search/"+ fragmentcode +"/" + suffix  + '?dependentParam=' + (dependentParam == undefined ? '' : dependentParam),
		"type": 'POST'
	},
	"columns": dataRetreiver,
	"search": {
		"search": searchValue
	}
});

//make ajax to call server
dt.draw();

$('.' + tablename).on('click', 'a.dataindex', function(e){
	e.preventDefault();

	console.log("here i m");

	if(mainscreen == true){
		$('#searchSuggestTableModal').modal('hide');


		var value = $(this).data('value');

		sectionReloadAjaxReq({
			id : mainreloadid,
			url : mainreloadurl + value
		});

		if(detailreloadid){
			sectionReloadAjaxReq({
				id : detailreloadid,
				url : detailreloadurl + value
			});
		}

		if(additionalreloadid){
			sectionReloadAjaxReq({
				id : additionalreloadid,
				url : additionalreloadurl + value
			});
		}
	} else {
		var prompt = $(this).data('prompt');
		var value = $(this).data('value');

		$('#searchSuggestTableModal').modal('hide');

		$('input[name="'+ fieldId +'"]').val(value);
		$('#' + fieldId).val(prompt);

		if(resetParam != undefined && resetParam != ''){
			var resetFields = resetParam.split(',');
			$.each(resetFields, function(i, j){
				if(j != undefined && j != '') {
					$('#' + j).val("");
					$('input[name="'+ j +'"]').val("");
				}
			});
		}
	}
})