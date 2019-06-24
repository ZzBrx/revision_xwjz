(function($) {
	var DataGrid = function(options, $this) {
		var dataGrid = typeof $this != "undefined" ? $this : options.dataGrid ? options.dataGrid : $("#dataGrid"),
			dataGridId = dataGrid.attr("id"),
			dataGridPage = options.dataGridPage ? options.dataGridPage : $("#" + dataGridId + "Page"),
			searchForm = options.searchForm ? options.searchForm : $("#searchForm"),
			options = $.extend({
				url: searchForm.attr("action"),
				postData: searchForm.serializeArray(),
				mtype: "POST",
				datatype: "json",
				jsonReader: {//用于设置如何解析从Server端发回来的json数据
					id: options.dataId,
					root: "list", //代表实际模型数据的入口
					page: "pageNum",//代表当前页码的数据
					total: "pages",//代表页码总数的数据
					records: "total", //代表数据行总数的数据
					userdata: "otherData",//其他数据，我们需要用的一些并不想显示到页面上的数据 
					subgrid: {
						root: "list"
					}
				},
				treeReader: {
					level_field: "treeLevel",
					parent_id_field: "parentId",
					userdata: "otherData",
					leaf_field: "treeLeaf",
					expanded_field: "isOpen",
					icon_field: "_icon"
				},
				prmNames: {//设置jqGrid将要向Server传递的参数名称
					page: "pageNum",//表示请求页码的参数名称
					rows: "pageSize", // 表示请求行数的参数名称
					sort: "orderBy", // 表示用于排序的列名的参数名称
					order: "sord",// 表示采用的排序方式的参数名称
					search: "_search", // 表示是否是搜索请求的参数名称
					nd: "nd",// 表示已经发送请求的次数的参数名称
					id: "id",// 表示当在编辑数据模块中发送数据时，使用的id的名称
					oper: "oper",// operation参数名称
					editoper: "edit",// 当在edit模式中提交数据时，操作的名称
					addoper: "add",// 当在add模式中提交数据时，操作的名称
					deloper: "del",// 当在delete模式中提交数据时，操作的名称
					subgridid: "id",// 当点击以载入数据到子表时，传递的数据名称
					npage: null,
					totalrows: "totalPage"// 表示需从Server得到总共多少行数据的参数名称，参见jqGrid选项中的rowTotal
				},
				editurl: "clientArray",
				rowNum: -1,
				rownumWidth: 30,
				altRows: true,
				columnModel: [],
				colNames: [],
				colModel: [],
				dataId: "id",
				lazyLoad: false,
				shrinkToFit: true,
				showRownum: true,
				showCheckbox: false,
				sortableColumn: true,
				autoGridHeight: true,
				autoGridHeightFix: 0,
				autoGridWidth: true,
				autoGridWidthFix: 0,
				
				btnSearch: $("#btnSearch"),
				btnRefreshTree: $("#btnRefreshTree"),
				btnExpandTreeNode: $("#btnExpandTreeNode"),
				btnCollapseTreeNode: $("#btnCollapseTreeNode"),
				
				treeGrid: false,
				treeGridModel: "adjacency",
				treeColumn: null,
				ExpandColClick: true,
				ExpandColumn: options.treeColumn,
				defaultExpandLevel: 0,
				initExpandLevel: options.defaultExpandLevel,
				expandNodeClearPostData: false,
				
				inputPageNum: $("#pageNum", searchForm),
				inputPageSize: $("#pageSize", searchForm),
				inputOrderBy: $("#orderBy", searchForm),
				
				beforeRequest: function(data) {
					switch (options.datatype.toLowerCase()) {
						case "json":
						case "jsonp":
						case "xml":
						case "script":
							if (options.url == undefined || options.url == "") {
								js.showMessage("设置searchForm表单指定错误或URL为空。");
								return false
							}
					}
					js.loading();
					if (options.treeGrid) {
						var postData = getParam("postData");
						if (postData.id) {
							setParam({
								postData: {
									id: postData.id
								}
							})
						} else {
							if (postData.nodeid) {
								if (options.expandNodeClearPostData == true) {
									setParam({
										postData: {
											parentId: postData.nodeid
										}
									})
								} else {
									var ps = {
										parentId: postData.nodeid
									};
									$.each(searchForm.serializeArray(), function(idx, item) {
										ps[item.name] = item.value
									});
									if (typeof options.expandNodeClearPostData == "string") {
										$.each(options.expandNodeClearPostData.split(","), function(idx, val) {
											if (val != "") {
												ps[val] = ""
											}
										})
									}
									setParam({
										postData: ps
									})
								}
							} else {
								setParam({
									postData: searchForm.serializeArray()
								})
							}
						}
					} else {
						setParam({
							url: searchForm.attr("action"),
							postData: searchForm.serializeArray()
						})
					}
					if (typeof options.ajaxLoad == "function") {
						options.ajaxLoad(data)
					}
					resizeDataGrid();
					$(".btn").attr("disabled", true);
					$(".ui-jqgrid .loading").remove()
				},
				loadComplete: function(data) {
					if (dataGridPage.length >= 1 && data) {
						data.funcParam = dataGridId;
						dataGridPage.html(pageHtml(data))
					}
					if (options.treeGrid) {
						if (dataGrid.expandNodeIds) {
							setTimeout(function() {
								if (dataGrid.expandNodeIds.length > 0) {
									$("#" + dataGrid.expandNodeIds.shift() + ":visible .tree-plus", dataGrid).click()
								} else {
									if (dataGrid.currentLevel < dataGrid.expandLevel) {
										dataGrid.currentLevel++;
										dataGrid.expandNodeIds = [];
										$(".jqgrow:visible .tree-plus", dataGrid).each(function() {
											var id = $(this).parents(".jqgrow").attr("id");
											dataGrid.expandNodeIds.push(id)
										});
										$("#" + dataGrid.expandNodeIds.shift() + ":visible .tree-plus", dataGrid).click()
									} else {
										dataGrid.expandNodeIds = null
									}
								}
							}, 10)
						} else {
							if (options.defaultExpandLevel && options.defaultExpandLevel > 0) {
								expandTreeNode(options.defaultExpandLevel);
								options.defaultExpandLevel = 0
							}
						}
						setParam({
							postData: {
								id: "",
								nodeid: ""
							}
						})
					}

					if (typeof options.ajaxSuccess == "function") {
						options.ajaxSuccess(data)
					}
					if (typeof options.btnEventBind == "function") {
						options.btnEventBind(dataGrid.find(".btnList"))
					}
					if (typeof options.btnMoreEventBind == "function") {
						options.btnMoreEventBind(dataGrid.find(".btnMore"))
					}
					if (data && data.message) {
						js.showMessage(data.message)
					}
					resizeDataGrid();
					$(".btn").attr("disabled", false);
					js.closeLoading()
				},
				loadError: function(data) {
					if (typeof options.ajaxError == "function") {
						options.ajaxError(data)
					}
					$(".btn").attr("disabled", false);
					js.showErrorMessage(data.responseText);
					js.closeLoading(0, true)
				},
				gridComplete: function() {
					if (typeof options.complete == "function") {
						options.complete()
					}
					resizeDataGrid()
				},
				onSortCol: function(index, iCol, sortorder) {
					if (options.inputOrderBy && options.inputOrderBy.length) {
						options.inputOrderBy.val(index + " " + sortorder)
					}
				},
				
				//btnList 按钮绑定事件
				btnEventBind: function(elements) {
					elements.each(function() {
						if ($(this).attr("data-click-binded") == undefined) {
							$(this).attr("data-click-binded", true);
							$(this).click(function() {
								var se = $(this);
								var url = se.attr("href");
								var title = se.data("title") || se.attr("title") ;
								var confirm = se.data("confirm");
								if (confirm != undefined) {
									js.confirm(confirm, url, function(data) {
										js.showMessage(data.message);
										if (data.result == "true") {	
											var confirmSuccess = se.data("confirmSuccess");
											if (confirmSuccess != undefined) {
												try {
													eval(confirmSuccess)
												} catch (e) {
													log("confirmSuccess error: " + e)
												}
											} else {
												if (options.treeGrid) {
													var delTreeNode = se.data("deltreenode");
													if (delTreeNode != undefined) {
														dataGrid.dataGrid("delTreeNode", delTreeNode)
													} else {
														var row = getRowData(se.parents(".jqgrow").attr("id"));
														if (row && !isRoot(row.parentId)) {
															refreshTreeChildren(row.parentId)
														} else {
															refreshTree()
														}
													}
												} else {
													refresh()
												}
											}
										}
									}, "json")
								} else {
									js.addTabPage($(this), title, url, options.tabPageId)
								}
								return false
							})
						}
					});
					return self
				},
				
				//没一行更多按钮
				btnMoreEventBind: function(elements) {
					elements.each(function() {
						if ($(this).attr("data-mouseover-binded") == undefined) {
							$(this).attr("data-mouseover-binded", true);
							var timeoutHover;
							elements.mouseover(function() {
								var bdiv = dataGrid.closest(".ui-jqgrid-bdiv"),//closest() 方法获得匹配选择器的第一个元素,.ui-jqgrid-bdiv是整个表格
									more = $(this), //btnMore 元素
									items = more.next(), //btnMore下一个元素moreItems
									pos = more.position(),//btnMore元素位置
									top = pos.top - items.height() / 2 + 6,
									left = pos.left - items.width() - 2;
								if (!items.hasClass("moreItems")) {
									return
								}
								dataGrid.find(".btnMore.open").removeClass("open").find("i").removeClass("fa-chevron-circle-left").addClass("fa-chevron-circle-right");
								dataGrid.find(".moreItems.open").removeClass("open").hide();
								var itemsTop = bdiv.scrollTop() + 5;
								if (top <= itemsTop) {
									top = itemsTop
								} else {
									itemsTop = bdiv.height() - items.height() + itemsTop - 12;
									if (top >= itemsTop) {
										top = itemsTop
									}
								}
								more.addClass("open").find("i").removeClass("fa-chevron-circle-right").addClass("fa-chevron-circle-left");
								items.addClass("open").css({
									top: top,
									left: left
								}).show();
								
								if (items.attr("data-hover-binded") == undefined) {
									items.attr("data-hover-binded", true);
									items.hover(
									function() {
										//阻止timeoutHover方法
										window.clearTimeout(timeoutHover)
									}, 
									function() {
										window.clearTimeout(timeoutHover);
										timeoutHover = window.setTimeout(function() {
											more.removeClass("open").find("i").removeClass("fa-chevron-circle-left").addClass("fa-chevron-circle-right");
											items.hide()
										}, 500)
									}).find("a").click(function() {
										more.removeClass("open").find("i").removeClass("fa-chevron-circle-left").addClass("fa-chevron-circle-right");
										items.hide()
									})
								}
							})
						}
					});
					return self
				},
				ajaxLoad: function(data) {},
				ajaxSuccess: function(data) {},
				ajaxError: function(data) {},
				complete: function() {}
			}, options);
		if (options.columnModel.length > 0) {
			options.colNames = [];
			options.colModel = [];
			for (var i = 0, model; i < options.columnModel.length; i++) {
				model = options.columnModel[i];
				if (model.header) {
					options.colNames.push(model.header)
				} else {
					if (model.label) {
						options.colNames.push(model.label)
					}
				}
				options.colModel.push(model)
			}
		}
		if (options.treeGrid || !options.sortableColumn) {
			for (var i = 0; i < options.colModel.length; i++) {
				options.colModel[i].sortable = false
			}
		}
		if (options.treeGrid) {
			options.showRownum = false;
			options.showCheckbox = true
		}
		if (options.showRownum) {
			options.rownumbers = true
		}
		if (options.showCheckbox) {
			options.multiselect = true
		}
		if (options.datatype == "local" && options.rowNum == -1) {
			options.rowNum = 5000
		}
		if (options.lazyLoad && !options.treeGrid) {
			options.datatype_bak = options.datatype;
			options.datatype = "local"
		}
		if (options.groupHeaders || options.frozenCols || !options.shrinkToFit) {
			options.shrinkToFit = false
		} else {
			options.shrinkToFit = true
		}
		if (options.showFooter) {
			options.footerrow = true;
			options.userDataOnFooter = true
		}
		if (options.inputPageNum.length == 0) {
			var pageNum = searchForm.data("pageNum");
			searchForm.append('<input id="pageNum" name="pageNum" type="hidden" value="' + (pageNum ? pageNum : "") + '"/>');
			options.inputPageNum = $("#pageNum", searchForm)
		}
		if (options.inputPageSize.length == 0) {
			var pageSize = searchForm.data("pageSize");
			searchForm.append('<input id="pageSize" name="pageSize" type="hidden" value="' + (pageSize ? pageSize : "") +
				'"/>');
			options.inputPageSize = $("#pageSize", searchForm)
		}
		if (options.inputOrderBy.length == 0) {
			var orderBy = searchForm.data("orderBy");
			searchForm.append('<input id="orderBy" name="orderBy" type="hidden" value="' + (orderBy ? orderBy : "") + '"/>');
			options.inputOrderBy = $("#orderBy", searchForm)
		}
		if (searchForm && searchForm.length > 0) {
			searchForm.submit(function() {
				refresh();
				return false
			}).on("reset", function() {
				setTimeout(function() {
					if ($.fn.iCheck !== undefined) {
						searchForm.find(
							"input[type=checkbox].form-control:not(.noicheck),input[type=radio].form-control:not(.noicheck)").iCheck("update")
					}
					if ($.fn.select2 !== undefined) {
						searchForm.find("select.form-control:not(.noselect2)").trigger("change")
					}
					searchForm.find(".isReset").val("")
				}, 200)
			})
		}
		if (typeof options.btnEventBind == "function") {
			options.btnEventBind($(".btnTool"))
		}
		if (options.btnSearch.length > 0) {
			options.btnSearch.unbind("click").click(function() {
				var btnSearch = $(this);
				if (searchForm.hasClass("hide")) {
					searchForm.removeClass("hide");
					btnSearch.addClass("active");
					btnSearch.html(btnSearch.html().replace("查询", "隐藏"))
				} else {
					searchForm.addClass("hide");
					btnSearch.removeClass("active");
					btnSearch.html(btnSearch.html().replace("隐藏", "查询"))
				}
				resizeDataGrid();
				return false
			});
			if (!searchForm.hasClass("hide")) {
				var btnSearch = options.btnSearch;
				searchForm.removeClass("hide");
				btnSearch.addClass("active");
				btnSearch.html(btnSearch.html().replace("查询", "隐藏"))
			}
		}
		
		if (options.treeGrid) {
			options.btnRefreshTree.unbind("click").click(function() {
				searchForm.each(function() {
					this.reset()
				});
				refreshTree();
				return false
			});
			options.btnExpandTreeNode.unbind("click").click(function() {
				expandTreeNode(1);
				return false
			});
			options.btnCollapseTreeNode.unbind("click").click(function() {
				collapseTreeNode();
				return false
			})
		}

		dataGrid.jqGrid(options);
		if (options.groupHeaders && options.groupHeaders.twoLevel) {
			if (options.groupHeaders.threeLevel) {
				dataGrid.jqGrid("setComplexHeaders", {
					complexHeaders: {
						defaultStyle: true,
						twoLevel: options.groupHeaders.twoLevel,
						threeLevel: options.groupHeaders.threeLevel
					}
				})
			} else {
				dataGrid.jqGrid("setGroupHeaders", {
					useColSpanStyle: true,
					groupHeaders: options.groupHeaders.twoLevel
				})
			}
		}
		if (options.frozenCols) {
			dataGrid.jqGrid("setFrozenColumns")
		}
		$(window).resizeEnd(function() {
			resizeDataGrid()
		});
		resizeDataGrid();

		function setGridHeight() {
			if (!options.autoGridHeight) {
				return
			}
			var gridHeight = 0;
			if (typeof options.autoGridHeight == "function") {
				gridHeight = options.autoGridHeight()
			} else {
				var tabContent = $(dataGrid).parents(".tab-content");
				if (tabContent.length > 0) {
					var portlet = $(dataGrid).parents(".box.tabbable.autoHeight:not(.default)");
					if (portlet.length > 0) {
						var headerHeight = $(dataGrid).parents(".ui-jqgrid").find(".ui-jqgrid-hdiv").height();
						if (headerHeight == 0) {
							headerHeight = 43;
							if (options.groupHeaders && options.groupHeaders.twoLevel) {
								if (options.groupHeaders.threeLevel) {
									headerHeight *= 3
								} else {
									headerHeight *= 2
								}
							}
						}
						gridHeight = tabContent.height() - headerHeight - options.autoGridHeightFix
					} else {
						gridHeight = 300
					}
				} else {
					var gridHeight = $(dataGrid).height();
					var gridParent = $(dataGrid).parent();
					if (gridParent.length != 0) {
						gridHeight = gridParent.height()
					}
					gridHeight = ($(window).height() - $("body").height() + gridHeight - options.autoGridHeightFix);
					if (gridHeight < 150) {
						gridHeight = 150
					}
					gridParent.height(gridHeight)
				}
			}
			if (gridHeight != 0) {
				dataGrid.jqGrid("setGridHeight", gridHeight)
			}
		}

		function setGridWidth() {
			if (!options.autoGridWidth) {
				return
			}
			var gridWidth = 0;
			if (typeof options.autoGridWidth == "function") {
				gridWidth = options.autoGridWidth()
			} else {
				var jqGridParent = $(dataGrid).parents(".ui-jqgrid").parent();
				if (jqGridParent.is(":visible")) {
					gridWidth = jqGridParent.width() - 2
				}
			}
			if (gridWidth != 0) {
				dataGrid.jqGrid("setGridWidth", gridWidth - options.autoGridWidthFix, (options.shrinkToFit && $(window).width() >
					500))
			}
		}

		function resizeDataGrid() {
			setGridHeight();
			setGridWidth();
			setTimeout(function() {
				setGridHeight();
				setGridWidth()
			}, (!!navigator.userAgent.match(/MSIE 8.0/)) ? 200 : 100)
		}

		function isRoot(code) {
			return !code || code == "" || code == "0"
		}

		function getParam(paramName) {
			return dataGrid.jqGrid("getGridParam", paramName)
		}

		function setParam(params, overwrite) {
			dataGrid.jqGrid("setGridParam", params, overwrite);
			return self
		}

		function getDataIDs() {
			return dataGrid.jqGrid("getDataIDs")
		}

		function getRowData(id) {
			return dataGrid.jqGrid("getRowData", id)
		}

		function getSelectRow() {
			return getParam("selrow")
		}

		function getSelectRows() {
			return getParam("selarrrow")
		}

		function setSelectRow(id, isCancel) {
			if (id == undefined && isCancel == undefined) {
				dataGrid.jqGrid("resetSelection")
			} else {
				if (id != undefined && isCancel) {
					dataGrid.jqGrid("resetSelection", id)
				} else {
					dataGrid.jqGrid("setSelection", id)
				}
			}
			return self
		}

		/**
		 * 刷新分页
		 * pageNum 页码
		 * pageSize 每页显示数量
		 */
		function refresh(pageNum, pageSize) {
			if (pageNum) {
				options.inputPageNum.val(pageNum)
			}
			if (pageSize) {
				options.inputPageSize.val(pageSize)
			}
			var params = {};
			if (options.inputPageNum.val() != "") {
				params.page = options.inputPageNum.val()
			}
			if (options.inputPageSize.val() != "") {
				params.rowNum = options.inputPageSize.val()
			}
			if (options.lazyLoad) {
				options.datatype = params.datatype = options.datatype_bak
			}
			setParam(params);
			//重新加载当前表格，也会向服务器发起新的请求
			reloadGrid();
			return self
		}

		//重新加载当前表格，也会向服务器发起新的请求
		function reloadGrid() {
			dataGrid.trigger("reloadGrid")
		}

		function delRowData(id) {
			dataGrid.jqGrid("delRowData", id);
			return self
		}

		function delTreeNode(id) {
			dataGrid.jqGrid("delTreeNode", id);
			return self
		}

		function expandTreeNode(level) {
			js.loading();
			setTimeout(function() {
				dataGrid.expandLevel = level;
				dataGrid.currentLevel = 1;
				dataGrid.expandNodeIds = [];
				$(".jqgrow:visible .tree-plus", dataGrid).each(function() {
					var id = $(this).parents(".jqgrow")[0].id;
					dataGrid.expandNodeIds.push(id)
				});
				if (dataGrid.expandNodeIds.length > 100) {
					js.showMessage("本次需要展开“" + dataGrid.expandNodeIds.length + "”个节点，因为展开节点过多，执行取消。");
					dataGrid.expandNodeIds = []
				} else {
					$("#" + dataGrid.expandNodeIds.shift() + ":visible .tree-plus", dataGrid).click()
				}
				for (var i = 0; i < dataGrid.expandLevel; i++) {
					$(".jqgrow:visible .tree-plus", dataGrid).click()
				}
				js.closeLoading()
			}, 10);
			return self
		}

		function collapseTreeNode() {
			js.loading();
			setTimeout(function() {
				$(".tree-minus", dataGrid).click();
				js.closeLoading()
			}, 10);
			return self
		}

		function refreshTree(expandLevel, parentId) {
			if (expandLevel) {
				options.defaultExpandLevel = expandLevel
			}
			if (!isRoot(parentId)) {
				setParam({
					postData: {
						id: parentId
					}
				})
			} else {
				if (parentId != undefined) {
					options.defaultExpandLevel = options.initExpandLevel
				}
			}
			reloadGrid();
			return self
		}

		function refreshTreeChildren(rowid, currentRowid) {
			if (currentRowid) {
				delRowData(currentRowid)
			}
			if (isRoot(rowid)) {
				reloadGrid(0, rowid)
			} else {
				if (rowid != "") {
					var treeclick = dataGrid.find("#" + rowid + " .treeclick");
					if (treeclick.hasClass("tree-leaf")) {
						treeclick.addClass("tree-parent")
					}
					treeclick.addClass("tree-reload").click();
					if (treeclick.hasClass("tree-plus")) {
						treeclick.click()
					}
				}
			}
		}

		function mergeCell(cellNames) {
			var trs = $("#" + dataGridId + ">tbody>tr:gt(0)");
			$.each(cellNames.split(","), function(idx, name) {
				var bg = trs.eq(0).children("[aria-describedby='" + dataGridId + "_" + name + "']"),
					index = bg.index(),
					rowsp = 1;
				trs.slice(1).each(function(ind2, tr) {
					var me = $(tr).children("td").eq(index);
					if (bg.text() === me.text()) {
						rowsp++;
						me.hide()
					} else {
						bg.attr("rowspan", rowsp);
						bg = me;
						rowsp = 1
					}
					bg.attr("rowspan", rowsp)
				})
			})
		}

		function updateListFieldName(listName, fieldNames) {
			$.each(fieldNames.split(","), function(idx, fieldName) {
				if (fieldName != "") {
					var inputs = ['[name="' + fieldName + '"]:not(div,a,script)', '[name^="' + listName + '["][name$="[].' +
						fieldName + '"]:not(div,a,script)'
					];
					dataGrid.find(inputs.join(",")).each(function(idx, val) {
						$(this).attr("name", listName + "[" + idx + "]." + fieldName)
					});
					var defaultInputs = ['[name="!' + fieldName + '"]:not(div,a,script)', '[name^="!' + listName +
						'["][name$="[].' + fieldName + '"]:not(div,a,script)'
					];
					dataGrid.find(defaultInputs.join(",")).each(function(idx, val) {
						$(this).attr("name", "!" + listName + "[" + idx + "]." + fieldName)
					})
				}
			})
		}
		
			
		
		var self = {
			jqGrid: function(option, value, v2, v3, v4, v5) {
				return dataGrid.jqGrid(option, value, v2, v3, v4, v5)
			},
			setParam: function(params, overwrite) {
				return setParam(params, overwrite)
			},
			getParam: function(paramName) {
				return getParam(paramName)
			},
			getDataIDs: function() {
				return getDataIDs()
			},
			getRowData: function(rowId) {
				return getRowData(rowId)
			},
			getSelectRow: function() {
				return getSelectRow()
			},
			getSelectRows: function() {
				return getSelectRows()
			},
			setSelectRow: function(id, isCancel) {
				return setSelectRow(id, isCancel)
			},
			refresh: function(pageNum, pageSize) {
				return refresh(pageNum, pageSize)
			},
			reloadGrid: function() {
				return reloadGrid()
			},
			delRowData: function(id) {
				return delRowData(id)
			},
			delTreeNode: function(id) {
				return delTreeNode(id)
			},
			expandTreeNode: function(level) {
				return expandTreeNode(level)
			},
			collapseTreeNode: function() {
				collapseTreeNode()
			},
			refreshTree: function(expandLevel, parentId) {
				return refreshTree(expandLevel, parentId)
			},
			refreshTreeChildren: function(rowid, currentRowid) {
				return refreshTreeChildren(rowid, currentRowid)
			},
			mergeCell: function(cellNames) {
				return mergeCell(cellNames)
			},
			updateListFieldName: function(listName, fieldNames) {
				updateListFieldName(listName, fieldNames)
			}
		};
		return self
	};
	
	//入口
	$.fn.dataGrid = function(option, value, v2, v3, v4, v5) {
		var method_call;
		var $set = this.each(function() {
			var $this = $(this);
			var data = $this.data("dataGrid");
			var options = typeof option === "object" && option;
			if (!data) {
				data = new DataGrid(options, $this);
				window[$this.attr("id")] = data;
				$this.data("dataGrid", data)
			}
			if (typeof option === "string" && typeof data[option] === "function") {
				if (value instanceof Array) {
					method_call = data[option].apply(data, value, v2, v3, v4, v5)
				} else {
					method_call = data[option](value, v2, v3, v4, v5)
				}
			}
		});
		if (method_call === undefined) {
			var fn = $.jgrid.getMethod(option);
			if (fn) {
				//将一个类数组对象转换为真正的数组对象
				var args = $.makeArray(arguments).slice(1);
				method_call = fn.apply(this, args)
			}
		}
		return (method_call === undefined) ? $set : method_call
	};
	$.extend($.jgrid, {
		stripHtml: function(v) {
			v = String(v);
			return v.replace(/<[^>]*>/g, "")
		}
	})
})(jQuery);


//分页
function page(pageNum, pageSize, c) {
	try {
		if (c && c != "") {
			$("#dataGrid").dataGrid("refresh", pageNum, pageSize)
		} else {
			$(".ui-jqgrid-btable:eq(0)").dataGrid("refresh", pageNum, pageSize)
		}
	} catch (d) {}
	return false
}

function pageHtml(pageInfo) {
	
	//首页
	var firstPage = 1;
	//尾页
	if (!pageInfo.pages) {
		pageInfo.pages = 1;
	}
	
	var centerNum = 5;
	
	//设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用。
	//var funcName = "page";
	
	
	// 函数的附加参数，第三个参数值。
	/*if (!c.funcParam) {
		c.funcParam = ""
	}*/
	
	//当前页码
	if (!pageInfo.pageNum) {
		pageInfo.pageNum = 1;
	}
	//每页数据条数
	if (!pageInfo.pageSize) {
		pageInfo.pageSize = 30;
	}
	
	//上一页索引
	if (!pageInfo.prePage || pageInfo.prePage < firstPage) {
		pageInfo.prePage = firstPage;
	}
	//下一页索引
	if (!pageInfo.nextPage || pageInfo.nextPage > pageInfo.pages) {
		pageInfo.nextPage = pageInfo.pages;
	}
	
	//数据总条数
	if (!pageInfo.total) {
		pageInfo.total = 0;
	}
	
	
	var bothNum = 1;
	
	//显示开始页码 = 当前页码- 需要显示的页码数/2
	var centerBegin = pageInfo.pageNum - parseInt(centerNum / 2);
	if (centerBegin < firstPage) {
		centerBegin = firstPage;
	}
	
	//显示结束页码 = 显示开始的页码+需要显示的页码数-1
	var centerEnd = centerBegin + centerNum - 1;
	//显示结束页码大于等于尾页页码
	if (centerEnd >= pageInfo.pages) {
		centerEnd = pageInfo.pages;
		//显示开始页码 = 显示结束页码 - 需要显示的页码数 +1
		centerBegin = centerEnd - centerNum + 1;
	}
	
	//显示开始页码 = 首页页码
	if (centerBegin == firstPage) {
		//显示结束页码 = 显示开始页码 + 1
		centerEnd = centerEnd + bothNum;
	}
	//显示结束页码 = 总页码
	if (centerEnd == pageInfo.pages) {
		//显示开始页码数 = 显示开始页码数 - 1
		centerBegin = centerBegin - bothNum;
	}
	
	//总页数-最后显示的页数 <= 1
	if (pageInfo.pages - centerEnd <= bothNum) {
		centerBegin--;
	}
	//显示开始的页码<首页码
	if (centerBegin < firstPage) {
		//显示开始的页码 = 首页码
		centerBegin = firstPage;
	}
	
	var html = ['<ul class="pagination">\n'];
	var a = 0;
	//如果当前页码 = 开始页码，隐藏左箭头
	if (pageInfo.pageNum == firstPage) {
		html.push('<li class="disabled"><a href="javascript:"><i class="fa fa-angle-left"></i></a></li>\n')
	} else {
		html.push('<li><a href="javascript:" onclick="page('+ pageInfo.prePage+","+ pageInfo.pageSize +');"> <i class="fa fa-angle-left"></i></a></li>\n');
	}
	
	for (a = firstPage; a < firstPage + bothNum && a < centerBegin; a++) {
		html.push('<li><a href="javascript:" onclick="page('+ pageInfo.prePage+","+ pageInfo.pageSize +');">' + (a + 1 - firstPage) + '</a></li>\n');
	}
	
	if (a < centerBegin) {
		html.push('<li class="disabled"><a href="javascript:">...</a></li>\n');
	} else {
		centerEnd++
	}
	
	if (centerEnd > pageInfo.pages) {
		centerEnd = pageInfo.pages
	}
	for (a = centerBegin; a <= centerEnd; a++) {
		//为当前页
		if (a == pageNum) {
			html.push('<li class="active"><a href="javascript:">' + (a + 1 - firstPage) + '</a></li>\n');
		} else {
			html.push('<li><a href="javascript:" onclick="page('+ a+","+ pageInfo.pageSize +');">' + (a + 1 - firstPage) + '</a></li>\n');
		}
	}
	
	//总页数-显示最后一个数>1
	if (pageInfo.pages - centerEnd > bothNum) {
		html.push('<li class="disabled"><a href="javascript:">...</a></li>\n');
		centerEnd = pageInfo.pages - bothNum
	}
	for (a = centerEnd + 1; a <= pageInfo.pages; a++) {
		html.push('<li><a href="javascript:" onclick="page('+ a +","+ pageInfo.pageSize +');">' + (a + 1 - firstPage) + '</a></li>\n');
	}
	//当前页码 = 最后一页
	if (pageInfo.pageNum == pageInfo.pages) {
		//隐藏向右箭头
		html.push('<li class="disabled"><a href="javascript:"><i class="fa fa-angle-right"></i></a></li>\n');
	} else {
		html.push('<li><a href="javascript:" onclick="page('+ pageInfo.nextPage+","+ pageInfo.pageSize +');"> <i class="fa fa-angle-right"></i></a></li>\n');
	}
	html.push('</ul>\n<ul class="pagination">\n');
	html.push('<li class="disabled controls" title="点击数字，可填写页码和每页条数，按回车即可生效！">当前 ');
	html.push('<input type="text" value="' + pageInfo.pageNum + '" onkeypress="var e=window.event||event;var c=e.keyCode||e.which;if(c==13)');
	html.push('page(this.value,' + pageInfo.pageSize + ');" onclick="this.select();"/> 页，每页 ');
	html.push('<input type="text" value="' + pageInfo.pageSize + '" onkeypress="var e=window.event||event;var c=e.keyCode||e.which;if(c==13)');
	html.push('page('+pageInfo.pageNum + ',this.value);"onclick="this.select();"/> 条，');
	html.push("共 " + pageInfo.total + " 条</li>\n");
	html.push("</ul>\n");
	html.push('<div style="clear:both;"></div>');
	
	return html.join("")
	
}
(function(a) {
	a.jgrid = a.jgrid || {};
	a.extend(a.jgrid, {
		defaults: {
			recordtext: "{0} - {1}\u3000共 {2} 条",
			emptyrecords: "无数据显示",
			loadtext: "正在加载...",
			pgtext: " {0} 共 {1} 页",
			pgfirst: "First Page",
			pglast: "Last Page",
			pgnext: "Next Page",
			pgprev: "Previous Page",
			pgrecs: "Records per Page",
			showhide: "Toggle Expand Collapse Grid"
		},
		search: {
			caption: "搜索...",
			Find: "查找",
			Reset: "重置",
			odata: [{
				oper: "eq",
				text: "等于\u3000\u3000"
			}, {
				oper: "ne",
				text: "不等\u3000\u3000"
			}, {
				oper: "lt",
				text: "小于\u3000\u3000"
			}, {
				oper: "le",
				text: "小于等于"
			}, {
				oper: "gt",
				text: "大于\u3000\u3000"
			}, {
				oper: "ge",
				text: "大于等于"
			}, {
				oper: "bw",
				text: "开始于"
			}, {
				oper: "bn",
				text: "不开始于"
			}, {
				oper: "in",
				text: "属于\u3000\u3000"
			}, {
				oper: "ni",
				text: "不属于"
			}, {
				oper: "ew",
				text: "结束于"
			}, {
				oper: "en",
				text: "不结束于"
			}, {
				oper: "cn",
				text: "包含\u3000\u3000"
			}, {
				oper: "nc",
				text: "不包含"
			}, {
				oper: "nu",
				text: "不存在"
			}, {
				oper: "nn",
				text: "存在"
			}],
			groupOps: [{
				op: "AND",
				text: "所有"
			}, {
				op: "OR",
				text: "任一"
			}],
			operandTitle: "Click to select search operation.",
			resetTitle: "Reset Search Value"
		},
		edit: {
			addCaption: "添加记录",
			editCaption: "编辑记录",
			bSubmit: "提交",
			bCancel: "取消",
			bClose: "关闭",
			saveData: "数据已改变，是否保存？",
			bYes: "是",
			bNo: "否",
			bExit: "取消",
			msg: {
				required: "此字段必需",
				number: "请输入有效数字",
				minValue: "输值必须大于等于 ",
				maxValue: "输值必须小于等于 ",
				email: "这不是有效的e-mail地址",
				integer: "请输入有效整数",
				date: "请输入有效时间",
				url: "无效网址。前缀必须为 ('http://' 或 'https://')",
				nodefined: " 未定义！",
				novalue: " 需要返回值！",
				customarray: "自定义函数需要返回数组！",
				customfcheck: "必须有自定义函数!"
			}
		},
		view: {
			caption: "查看记录",
			bClose: "关闭"
		},
		del: {
			caption: "删除",
			msg: "删除所选记录？",
			bSubmit: "删除",
			bCancel: "取消"
		},
		nav: {
			edittext: "",
			edittitle: "编辑所选记录",
			addtext: "",
			addtitle: "添加新记录",
			deltext: "",
			deltitle: "删除所选记录",
			searchtext: "",
			searchtitle: "查找",
			refreshtext: "",
			refreshtitle: "刷新表格",
			alertcap: "注意",
			alerttext: "请选择记录",
			viewtext: "",
			viewtitle: "查看所选记录"
		},
		col: {
			caption: "选择列",
			bSubmit: "确定",
			bCancel: "取消"
		},
		errors: {
			errcap: "错误",
			nourl: "没有设置url",
			norecords: "没有要处理的记录",
			model: "colNames 和 colModel 长度不等！"
		},
		formatter: {
			integer: {
				thousandsSeparator: ",",
				defaultValue: "0"
			},
			number: {
				decimalSeparator: ".",
				thousandsSeparator: ",",
				decimalPlaces: 2,
				defaultValue: "0.00"
			},
			currency: {
				decimalSeparator: ".",
				thousandsSeparator: ",",
				decimalPlaces: 2,
				prefix: "",
				suffix: "",
				defaultValue: "0.00"
			},
			date: {
				dayNames: ["日", "一", "二", "三", "四", "五", "六", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", ],
				monthNames: ["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二", "一月", "二月", "三月", "四月", "五月", "六月",
					"七月", "八月", "九月", "十月", "十一月", "十二月"
				],
				AmPm: ["am", "pm", "上午", "下午"],
				S: function(b) {
					return b < 11 || b > 13 ? ["st", "nd", "rd", "th"][Math.min((b - 1) % 10, 3)] : "th"
				},
				srcformat: "Y-m-d",
				newformat: "Y-m-d",
				parseRe: /[#%\\\/:_;.,\t\s-]/,
				masks: {
					ISO8601Long: "Y-m-d H:i:s",
					ISO8601Short: "Y-m-d",
					ShortDate: "n/j/Y",
					LongDate: "l, F d, Y",
					FullDateTime: "l, F d, Y g:i:s A",
					MonthDay: "F d",
					ShortTime: "g:i A",
					LongTime: "g:i:s A",
					SortableDateTime: "Y-m-d\\TH:i:s",
					UniversalSortableDateTime: "Y-m-d H:i:sO",
					YearMonth: "F, Y"
				},
				reformatAfterEdit: false,
				userLocalTime: false
			},
			baseLinkUrl: "",
			showAction: "",
			target: "",
			checkbox: {
				disabled: true
			},
			idName: "id"
		}
	})
})(jQuery);
