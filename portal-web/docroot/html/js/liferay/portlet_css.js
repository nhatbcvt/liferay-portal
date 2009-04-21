Liferay.PortletCSS = {
	init: function(portletId) {
		var instance = this;

		var curPortletBoundaryId = 'p_p_id_' + portletId + '_';
		var obj = jQuery('#' + curPortletBoundaryId);

		instance._portletId = portletId;
		instance._curPortlet = obj.find('.portlet');
		instance._curPortletWrapperId = instance._curPortlet.attr('id');
		instance._portletBoundaryId = curPortletBoundaryId;
		instance._newPanel = jQuery('#portlet-set-properties');
		instance._currentLanguage = themeDisplay.getLanguageId();

		if (!instance._curPortlet.length) {
			instance._curPortlet = obj;
			instance._curPortletWrapperId = curPortletBoundaryId;
		}

		if (instance._curPortlet.length) {
			var content = instance._newPanel;

			if (!content.length) {
				content = jQuery('<div class="loading-animation" />')
			}

			if (!instance._currentPopup) {
				instance._currentPopup = new Expanse.Popup(
					{
						body: content[0],
						height: 'auto',
						on: {
							close: function() {
								instance._newPanel.removeClass('instantiated');
								instance._newPanel.appendTo('body');
								instance._newPanel.hide();

								instance._currentPopup = null;

								if (Liferay.Browser.isIe() && Liferay.Browser.getMajorVersion() == 6) {
									window.location.reload(true);
								}
							}
						},
						width: 820,
						xy: [20, 20]
					}
				);

				instance._currentPopupBody = jQuery(instance._currentPopup.body);
			}

			if (!instance._newPanel.length) {
				jQuery.ajax(
					{
						url: themeDisplay.getPathMain() + '/portal/render_portlet',
						data: {
							p_l_id: themeDisplay.getPlid(),
							p_p_id: 113,
							p_p_state: 'exclusive',
							doAsUserId: themeDisplay.getDoAsUserIdEncoded()
						},
						success: function(message) {
							instance._currentPopupBody.html(message);
							instance._newPanel = instance._currentPopupBody.find('#portlet-set-properties');
							instance._loadContent();
						}
					}
				);
			}
			else {
				instance._loadContent(true);
			}
		}
	},

	_assignColorPickers: function() {
		var instance = this;

		instance._newPanel.find('.use-colorpicker').each(
			function() {
				new Expanse.ColorPickerPanel(
					{
						buttonbuttonContext: jQuery('#portlet-set-properties')[0],
						item: this
					}
				);
			}
		);
	},

	_backgroundStyles: function() {
		var instance = this;

		var bgData = instance._objData.bgData;

		var portlet = instance._curPortlet;

		// Background color

		var backgroundColor = instance._backgroundColor;

		var setColor = function(obj) {
			var color = obj.value;

			var cssColor = color;

			if ((color == '') || (color == '#')) {
				cssColor = 'transparent';
				color = '';
			}

			portlet.css('background-color', cssColor);
			bgData.backgroundColor = color;
		};

		var colorPicker = new Expanse.ColorPickerPanel(
			{
				buttonContext: instance._newPanel[0],
				item: backgroundColor[0],
				on: {
					change: function() {
						setColor(backgroundColor[0]);
					}
				}
			}
		);

		backgroundColor.unbind('blur.liferay').bind(
			'blur.liferay',
			function() {
				setColor(this);
			}
		);

		// Background image

		var useBgImage = instance._useBgImage;
		var useBg = useBgImage.is(':checked');
		var bgImageProperties = instance._bgImageProperties;

		bgData.useBgImage = useBg;

		if (useBg) {
			bgImageProperties.show();
		}

		useBgImage.unbind('click.liferay').bind(
			'click.liferay',
			function() {
				bgImageProperties.toggle();
				bgData.useBgImage = this.checked;
			}
		);

		// Background repeating

		var bgRepeating = instance._bgRepeating;

		bgRepeating.unbind('change.liferay').bind(
			'change.liferay',
			function() {
				var backgroundRepeat = this.options[this.selectedIndex].value;
				portlet.css('background-repeat', backgroundRepeat);

				bgData.backgroundRepeat = backgroundRepeat;
			}
		);

		// Background position

		var bgPosTop = instance._bgPosTop;
		var bgPosTopUnit = instance._bgPosTopUnit;

		var bgPosLeft = instance._bgPosLeft;
		var bgPosLeftUnit = instance._bgPosLeftUnit;

		var updatePos = function() {
			var topPos = instance._getCombo(bgPosTop, bgPosTopUnit);
			var leftPos = instance._getCombo(bgPosLeft, bgPosLeftUnit);

			portlet.css('background-position', leftPos.both + ' ' + topPos.both);

			bgData.backgroundPosition.top.value = topPos.input;
			bgData.backgroundPosition.top.unit = topPos.selectBox;

			bgData.backgroundPosition.left.value = leftPos.input;
			bgData.backgroundPosition.left.unit = leftPos.selectBox;
		};

		bgPosTop.unbind('blur.liferay').bind('blur.liferay', updatePos);
		bgPosLeft.unbind('blur.liferay').bind('blur.liferay', updatePos);
		bgPosTop.unbind('keyup.liferay').bind('keyup.liferay', updatePos);
		bgPosLeft.unbind('keyup.liferay').bind('keyup.liferay', updatePos);

		bgPosTopUnit.unbind('change.liferay').bind('change.liferay', updatePos);
		bgPosLeftUnit.unbind('change.liferay').bind('change.liferay', updatePos);
	},

	_borderStyles: function() {
		var instance = this;

		var portlet = instance._curPortlet;

		var ufaWidth = instance._ufaBorderWidth;
		var ufaStyle = instance._ufaBorderStyle;
		var ufaColor = instance._ufaBorderColor;

		var borderData = instance._objData.borderData;

		// Border width

		var wTopInt = instance._borderTopInt;
		var wTopUnit = instance._borderTopUnit;
		var wRightInt = instance._borderRightInt;
		var wRightUnit = instance._borderRightUnit;
		var wBottomInt = instance._borderBottomInt;
		var wBottomUnit = instance._borderBottomUnit;
		var wLeftInt = instance._borderLeftInt;
		var wLeftUnit = instance._borderLeftUnit;

		var changeWidth = function() {
			var styling = {};
			var borderWidth = {};

			borderWidth = instance._getCombo(wTopInt, wTopUnit);
			styling = {borderWidth: borderWidth.both};

			var ufa = ufaWidth.is(':checked');

			borderData.borderWidth.top.value = borderWidth.input;
			borderData.borderWidth.top.unit = borderWidth.selectBox;
			borderData.borderWidth.sameForAll = ufa;

			if (!ufa) {
				var extStyling = {};

				extStyling.borderTopWidth = styling.borderWidth;

				var right = instance._getCombo(wRightInt, wRightUnit);
				var bottom = instance._getCombo(wBottomInt, wBottomUnit);
				var left = instance._getCombo(wLeftInt, wLeftUnit);

				extStyling.borderRightWidth = right.both;
				extStyling.borderBottomWidth = bottom.both;
				extStyling.borderLeftWidth = left.both;

				styling = extStyling;

				borderData.borderWidth.right.value = right.input;
				borderData.borderWidth.right.unit = right.selectBox;

				borderData.borderWidth.bottom.value = bottom.input;
				borderData.borderWidth.bottom.unit = bottom.selectBox;

				borderData.borderWidth.left.value = left.input;
				borderData.borderWidth.left.unit = left.selectBox;
			}

			portlet.css(styling);

			changeStyle();
			changeColor();
		};

		wTopInt.unbind('blur.liferay').bind('blur.liferay', changeWidth);
		wTopInt.unbind('keyup.liferay').bind('keyup.liferay', changeWidth);

		wRightInt.unbind('blur.liferay').bind('blur.liferay', changeWidth);
		wRightInt.unbind('keyup.liferay').bind('keyup.liferay', changeWidth);

		wBottomInt.unbind('blur.liferay').bind('blur.liferay', changeWidth);
		wBottomInt.unbind('keyup.liferay').bind('keyup.liferay', changeWidth);

		wLeftInt.unbind('blur.liferay').bind('blur.liferay', changeWidth);
		wLeftInt.unbind('keyup.liferay').bind('keyup.liferay', changeWidth);

		wTopUnit.unbind('change.liferay').bind('change.liferay', changeWidth);
		wRightUnit.unbind('change.liferay').bind('change.liferay', changeWidth);
		wBottomUnit.unbind('change.liferay').bind('change.liferay', changeWidth);
		wLeftUnit.unbind('change.liferay').bind('change.liferay', changeWidth);

		ufaWidth.unbind('click.liferay').bind('click.liferay', changeWidth);

		// Border style

		var sTopStyle = instance._borderTopStyle;
		var sRightStyle = instance._borderRightStyle;
		var sBottomStyle = instance._borderBottomStyle;
		var sLeftStyle = instance._borderLeftStyle;

		var changeStyle = function() {
			var styling = {};
			var borderStyle = {};

			borderStyle = sTopStyle.find('option:selected').val();
			styling = {borderStyle: borderStyle};
			var ufa = ufaStyle.is(':checked');

			borderData.borderStyle.top = borderStyle;
			borderData.borderStyle.sameForAll = ufa;

			if (!ufa) {
				var extStyling = {};

				extStyling.borderTopStyle = styling.borderStyle;

				var right = sRightStyle.find('option:selected').val();
				var bottom = sBottomStyle.find('option:selected').val();
				var left = sLeftStyle.find('option:selected').val();

				extStyling.borderRightStyle = right;
				extStyling.borderBottomStyle = bottom;
				extStyling.borderLeftStyle = left;

				styling = extStyling;

				borderData.borderStyle.right = right;

				borderData.borderStyle.bottom = bottom;

				borderData.borderStyle.left = left;
			}

			portlet.css(styling);
		};

		sTopStyle.unbind('change.liferay').bind('change.liferay', changeStyle);
		sRightStyle.unbind('change.liferay').bind('change.liferay', changeStyle);
		sBottomStyle.unbind('change.liferay').bind('change.liferay', changeStyle);
		sLeftStyle.unbind('change.liferay').bind('change.liferay', changeStyle);

		ufaStyle.unbind('click.liferay').bind('click.liferay', changeStyle);

		// Border color

		var cTopColor = instance._borderTopColor;
		var cRightColor = instance._borderRightColor;
		var cBottomColor = instance._borderBottomColor;
		var cLeftColor = instance._borderLeftColor;

		var changeColor = function() {
			var styling = {};
			var borderColor = {};

			borderColor = cTopColor.val();
			styling = {borderColor: borderColor};

			var ufa = ufaColor.is(':checked');

			borderData.borderColor.top = borderColor;
			borderData.borderColor.sameForAll = ufa;

			if (!ufa) {
				var extStyling = {};

				extStyling.borderTopColor = styling.borderColor;

				var right = cRightColor.val();
				var bottom = cBottomColor.val();
				var left = cLeftColor.val();

				extStyling.borderRightColor = right;
				extStyling.borderBottomColor = bottom;
				extStyling.borderLeftColor = left;

				styling = extStyling;

				borderData.borderColor.right = right;

				borderData.borderColor.bottom = bottom;

				borderData.borderColor.left = left;
			}

			portlet.css(styling);
		};

		var colorPickerTop = new Expanse.ColorPickerPanel(
			{
				buttonContext: jQuery('#portlet-set-properties')[0],
				item: cTopColor[0],
				on: {
					change: changeColor
				}
			}
		);

		var colorPickerRight = new Expanse.ColorPickerPanel(
			{
				buttonContext: jQuery('#portlet-set-properties')[0],
				item: cRightColor[0],
				on: {
					change: changeColor
				}
			}
		);

		var colorPickerBottom = new Expanse.ColorPickerPanel(
			{
				buttonContext: jQuery('#portlet-set-properties')[0],
				item: cBottomColor[0],
				on: {
					change: changeColor
				}
			}
		);

		var colorPickerLeft = new Expanse.ColorPickerPanel(
			{
				buttonContext: jQuery('#portlet-set-properties')[0],
				item: cLeftColor[0],
				on: {
					change: changeColor
				}
			}
		);

		cTopColor.unbind('blur.liferay').bind('blur.liferay', changeColor);
		cRightColor.unbind('blur.liferay').bind('blur.liferay', changeColor);
		cBottomColor.unbind('blur.liferay').bind('blur.liferay', changeColor);
		cLeftColor.unbind('blur.liferay').bind('blur.liferay', changeColor);

		cTopColor.unbind('keyup.liferay').bind('keyup.liferay', changeColor);
		cRightColor.unbind('keyup.liferay').bind('keyup.liferay', changeColor);
		cBottomColor.unbind('keyup.liferay').bind('keyup.liferay', changeColor);
		cLeftColor.unbind('keyup.liferay').bind('keyup.liferay', changeColor);

		ufaColor.unbind('click.liferay').bind('click.liferay', changeColor);

	},

	_cssStyles: function() {
		var instance = this;

		var portlet = instance._curPortlet;

		var customCSS = jQuery('#lfr-custom-css');
		var customCSSContainer = customCSS.parents('.ctrl-holder');
		var customPortletNoteHTML = '<p class="portlet-msg-info form-hint"></p>';
		var customPortletNote = jQuery('#lfr-portlet-info');
		var refreshText = '';

		var portletId = instance._curPortletWrapperId;
		var portletClasses = portlet.attr('class');

		portletClasses = jQuery.trim(portletClasses).replace(/(\s)/g, '$1.');

		var portletInfoText =
			Liferay.Language.get('your-current-portlet-information-is-as-follows') + ':<br />' +
				Liferay.Language.get('portlet-id') + ': <strong>#' + portletId + '</strong><br />' +
					Liferay.Language.get('portlet-classes') + ': <strong>.' + portletClasses + '</strong>';

		var customNote = jQuery('#lfr-refresh-styles');

		if (!customNote.length) {
			customNote = jQuery(customPortletNoteHTML);
			customNote.attr(
				{
					'class': '',
					id: 'lfr-refresh-styles'
				}
			);
		}

		if (!customPortletNote.length) {
			customPortletNote = jQuery(customPortletNoteHTML);
			customCSSContainer.before(customPortletNote);

			customPortletNote.attr(
				{
					id: 'lfr-portlet-info'
				}
			);
		}

		customPortletNote.html(portletInfoText);

		Liferay.Util.enableTextareaTabs(customCSS[0]);

		if (!Liferay.Browser.isSafari()) {
			refreshText = Liferay.Language.get('update-the-styles-on-this-page');

			var refreshLink = jQuery('<a href="javascript:;">' + refreshText + '</a>');

			var customStyleBlock = jQuery('#lfr-custom-css-block-' + portletId);

			if (!customStyleBlock.length) {

				// Do not modify. This is a workaround for an IE bug.

				var styleEl = document.createElement('style');

				styleEl.id = 'lfr-custom-css-block-' + portletId;
				styleEl.className = 'lfr-custom-css-block';
				styleEl.setAttribute('type', 'text/css');

				document.getElementsByTagName('head')[0].appendChild(styleEl);
			}
			else {
				styleEl = customStyleBlock[0];
			}

			var refreshStyles = function() {
				var customStyles = customCSS.val();

				customStyles = customStyles.replace(/<script[^>]*>([\u0001-\uFFFF]*?)<\/script>/gim, '');
				customStyles = customStyles.replace(/<\/?[^>]+>/gi, '');

				if (styleEl.styleSheet) { // for IE only
					if (customStyles == '') {

						// Do not modify. This is a workaround for an IE bug.

						customStyles = '<!---->';
					}
					styleEl.styleSheet.cssText = customStyles;
				}
				else {
					jQuery(styleEl).html(customStyles);
				}
			};

			refreshLink.unbind('click.liferay').bind('click.liferay', refreshStyles);

			customNote.empty().append(refreshLink);
		}
		else {
			refreshText = Liferay.Language.get('please-press-the-save-button-to-view-your-changes');

			customNote.empty().text(refreshText);
		}

		var insertContainer = jQuery('#lfr-add-rule-container');
		var addIdLink = jQuery('#lfr-add-id');
		var addClassLink = jQuery('#lfr-add-class');
		var updateOnType = jQuery('#lfr-update-on-type');

		if (!insertContainer.length) {
			insertContainer = jQuery('<div id="lfr-add-rule-container"></div>');
			addIdLink = jQuery('<a href="javascript:;" id="lfr-add-id">' + Liferay.Language.get('add-a-css-rule-for-just-this-portlet') + '</a>');
			addClassLink = jQuery('<a href="javascript:;" id="lfr-add-class">' + Liferay.Language.get('add-a-css-rule-for-all-portlets-like-this-one') + '</a>');

			var updateOnTypeHolder = jQuery('<div class="ctrl-holder"></div>');
			var updateOnTypeLabel = jQuery('<label>' + Liferay.Language.get('update-my-styles-as-i-type') + ' </label>');

			updateOnType = jQuery('<input id="lfr-update-on-type" type="checkbox" />');

			updateOnTypeLabel.append(updateOnType);
			updateOnTypeHolder.append(updateOnTypeLabel);

			customCSSContainer.after(insertContainer);

			insertContainer.append(addIdLink);
			insertContainer.append('<br />');
			insertContainer.append(addClassLink);
			insertContainer.append(updateOnTypeHolder);

			insertContainer.after(customNote);
		}

		updateOnType.bind(
			'click.liferay',
			function() {
				if (this.checked) {
					customNote.hide();
					customCSS.bind('keyup.liferay', refreshStyles);
				}
				else {
					customNote.show();
					customCSS.unbind('keyup.liferay', refreshStyles);
				}
			}
		);

		addIdLink.unbind('click.liferay').bind(
			'click.liferay',
			function() {
				customCSS[0].value += '\n#' + portletId + '{\n\t\n}\n';
			}
		);

		addClassLink.unbind('click.liferay').bind(
			'click.liferay',
			function() {
				customCSS[0].value += '\n.' + portletClasses.replace(/\s/g, '') + '{\n\t\n}\n';
			}
		);
	},

	_getCombo: function(input, selectBox) {
		var instance = this;

		var inputVal = input.val();
		var selectVal = selectBox.find('option:selected').val();

		inputVal = instance._getSafeInteger(inputVal);

		return {input: inputVal, selectBox: selectVal, both: inputVal + selectVal};
	},

	_getSafeInteger: function(input) {
		var instance = this;

		var output = parseInt(input);

		if (output == '' || isNaN(output)) {
			output = 0;
		}

		return output;
	},

	_languageClasses: function(key, value, removeClass) {
		var instance = this;

		var option = instance._portletLanguage.find('option[value=' + key + ']');

		if (removeClass) {
			option.removeClass('focused');
		}
		else {
			option.addClass('focused');
		}
	},

	_loadContent: function(instantiated) {
		var instance = this;

		var newPanel = instance._newPanel;
		var newPanelTabs = newPanel.find('> ul.ui-tabs');

		var tabTrigger = 0;

		if (!instantiated) {
			newPanel.addClass('instantiated');
			instance._portletBoundaryIdVar = jQuery('#portlet-boundary-id');

			// Portlet config

			instance._customTitleInput = jQuery('#custom-title');
			instance._defaultPortletTitle = instance._curPortlet.find('.portlet-title').eq(0).text();
			instance._customTitleCheckbox = jQuery('#use-custom-title-checkbox');
			instance._showBorders = jQuery('#show-borders');
			instance._borderNote = jQuery('#border-note');
			instance._portletLanguage = jQuery('#lfr-portlet-language');
			instance._portletLinksTarget = jQuery('#lfr-point-links');

			// Text

			instance._fontFamily = jQuery('#lfr-font-family');
			instance._fontWeight = jQuery('#lfr-font-bold');
			instance._fontStyle = jQuery('#lfr-font-italic');
			instance._fontSize = jQuery('#lfr-font-size');
			instance._fontColor = jQuery('#lfr-font-color');
			instance._textAlign = jQuery('#lfr-font-align');
			instance._textDecoration = jQuery('#lfr-font-decoration');
			instance._wordSpacing = jQuery('#lfr-font-space');
			instance._leading = jQuery('#lfr-font-leading');
			instance._tracking = jQuery('#lfr-font-tracking');

			// Background

			instance._backgroundColor = jQuery('#lfr-bg-color');

			instance._useBgImage = jQuery('#lfr-use-bg-image');
			instance._bgImageProperties = jQuery('.lfr-bg-image-properties');

			instance._bgRepeating = jQuery('#lfr-bg-repeat');

			instance._bgPosTop = jQuery('#lfr-bg-top-int');
			instance._bgPosTopUnit = jQuery('#lfr-bg-top-unit');
			instance._bgPosLeft = jQuery('#lfr-bg-left-int');
			instance._bgPosLeftUnit = jQuery('#lfr-bg-left-unit');

			// Border

			instance._ufaBorderWidth = jQuery('#lfr-use-for-all-width');
			instance._ufaBorderStyle = jQuery('#lfr-use-for-all-style');
			instance._ufaBorderColor = jQuery('#lfr-use-for-all-color');

			instance._borderTopInt = jQuery('#lfr-border-width-top');
			instance._borderTopUnit = jQuery('#lfr-border-width-top-unit');
			instance._borderRightInt = jQuery('#lfr-border-width-right');
			instance._borderRightUnit = jQuery('#lfr-border-width-right-unit');
			instance._borderBottomInt = jQuery('#lfr-border-width-bottom');
			instance._borderBottomUnit = jQuery('#lfr-border-width-bottom-unit');
			instance._borderLeftInt = jQuery('#lfr-border-width-left');
			instance._borderLeftUnit = jQuery('#lfr-border-width-left-unit');

			instance._borderTopStyle = jQuery('#lfr-border-style-top');
			instance._borderRightStyle = jQuery('#lfr-border-style-right');
			instance._borderBottomStyle = jQuery('#lfr-border-style-bottom');
			instance._borderLeftStyle = jQuery('#lfr-border-style-left');

			instance._borderTopColor = jQuery('#lfr-border-color-top');
			instance._borderRightColor = jQuery('#lfr-border-color-right');
			instance._borderBottomColor = jQuery('#lfr-border-color-bottom');
			instance._borderLeftColor = jQuery('#lfr-border-color-left');

			// Spacing

			instance._ufaPadding = jQuery('#lfr-use-for-all-padding');
			instance._ufaMargin = jQuery('#lfr-use-for-all-margin');

			instance._paddingTopInt = jQuery('#lfr-padding-top');
			instance._paddingTopUnit = jQuery('#lfr-padding-top-unit');
			instance._paddingRightInt = jQuery('#lfr-padding-right');
			instance._paddingRightUnit = jQuery('#lfr-padding-right-unit');
			instance._paddingBottomInt = jQuery('#lfr-padding-bottom');
			instance._paddingBottomUnit = jQuery('#lfr-padding-bottom-unit');
			instance._paddingLeftInt = jQuery('#lfr-padding-left');
			instance._paddingLeftUnit = jQuery('#lfr-padding-left-unit');

			instance._marginTopInt = jQuery('#lfr-margin-top');
			instance._marginTopUnit = jQuery('#lfr-margin-top-unit');
			instance._marginRightInt = jQuery('#lfr-margin-right');
			instance._marginRightUnit = jQuery('#lfr-margin-right-unit');
			instance._marginBottomInt = jQuery('#lfr-margin-bottom');
			instance._marginBottomUnit = jQuery('#lfr-margin-bottom-unit');
			instance._marginLeftInt = jQuery('#lfr-margin-left');
			instance._marginLeftUnit = jQuery('#lfr-margin-left-unit');

			// Advanced CSS

			instance._customCSS = jQuery('#lfr-custom-css');

			instance._saveButton = jQuery('#lfr-lookfeel-save');
			instance._resetButton = jQuery('#lfr-lookfeel-reset');

			// WAP styling

			instance._wapTitleInput = jQuery('#lfr-wap-title');
			instance._wapInitialWindowStateSelect = jQuery('#lfr-wap-initial-window-state');

		}

		newPanel.show();

		var tabs = new Expanse.TabView(newPanel[0]);

		tabs.selectTab(tabTrigger);

		newPanel.find('.lfr-colorpicker-img').remove();

		instance._portletMsgResponse = jQuery('#lfr-portlet-css-response');
		instance._portletMsgResponse.hide();

		var defaultData = {
			advancedData: {
				customCSS: ''
			},

			bgData: {
				backgroundColor: '',
				backgroundImage: '',
				useBgImage: false,
				backgroundRepeat: '',
				backgroundPosition: {
					left: {
						value: '',
						unit: 'px'
					},
					top: {
						value: '',
						unit: 'px'
					}
				}
			},

			borderData: {
				borderWidth: {
					bottom: {
						value: '',
						unit: 'px'
					},
					left: {
						value: '',
						unit: 'px'
					},
					right: {
						value: '',
						unit: 'px'
					},
					top: {
						value: '',
						unit: 'px'
					},
					sameForAll: true
				},

				borderStyle: {
					bottom: '',
					left: '',
					right: '',
					top: '',
					sameForAll: true
				},

				borderColor: {
					bottom: '',
					left: '',
					right: '',
					top: '',
					sameForAll: true
				}
			},

			portletData: {
				language: 'en_US',
				portletLinksTarget: '',
				showBorders: themeDisplay.getPortletSetupShowBordersDefault(),
				title: '',
				titles: {},
				useCustomTitle: false
			},

			spacingData: {
				margin: {
					bottom: {
						value: '',
						unit: 'px'
					},
					left: {
						value: '',
						unit: 'px'
					},
					right: {
						value: '',
						unit: 'px'
					},
					top: {
						value: '',
						unit: 'px'
					},
					sameForAll: true
				},
				padding: {
					bottom: {
						value: '',
						unit: 'px'
					},
					left: {
						value: '',
						unit: 'px'
					},
					right: {
						value: '',
						unit: 'px'
					},
					top: {
						value: '',
						unit: 'px'
					},
					sameForAll: true
				}

			},

			textData: {
				textAlign: '',
				color: '',
				fontFamily: '',
				fontSize: '',
				fontStyle: '',
				fontWeight: '',
				letterSpacing: '',
				lineHeight: '',
				textDecoration: '',
				wordSpacing: ''
			},

			wapData: {
				title: '',
				initialWindowState: 'NORMAL'
			}
		};

		var objectData = jQuery.ajax(
			{
				url: themeDisplay.getPathMain() + '/portlet_configuration/get_look_and_feel',
				data: {
					p_l_id: themeDisplay.getPlid(),
					doAsUserId: themeDisplay.getDoAsUserIdEncoded(),
					portletId: instance._portletId
				},
				async: false,
				dataType: 'json',
				type: "POST"
			}
		);

		if (objectData.responseText.length) {
			objectData = jQuery.parseJSON(objectData.responseText);

			instance._objData = objectData;
		}
		else {
			instance._objData = defaultData;
		}

		instance._assignColorPickers();

		instance._portletBoundaryIdVar.val(instance._curPortletWrapperId);

		instance._setDefaults();

		instance._portletConfig();
		instance._textStyles();
		instance._backgroundStyles();
		instance._borderStyles();
		instance._spacingStyles();
		instance._cssStyles();

		var useForAll = newPanel.find('.lfr-use-for-all');

		var handleForms = function() {
			var checkBox = jQuery(this);
			var otherHolders = checkBox.parents('fieldset:first').find('.ctrl-holder:gt(1)');
			var otherForms = otherHolders.find('input, select');
			var colorPickerImages = otherHolders.find('.lfr-colorpicker-img');

			if (this.checked) {
				otherHolders.fadeTo('fast', 0.3);
				otherForms.attr('disabled', true);
				colorPickerImages.hide();
			}
			else {
				otherHolders.fadeTo('fast', 1);
				otherForms.attr('disabled', false);
				colorPickerImages.show();
			}
		};

		useForAll.unbind('click.liferay', handleForms).bind('click.liferay', handleForms);
		useForAll.each(handleForms);

		var saveHandler = function(xHR, type) {
			var ajaxResponseMsg = instance._portletMsgResponse;
			var ajaxResponseHTML = '<div id="lfr-portlet-css-response"></div>';
			var message = '';
			var messageClass = '';

			if (type == 'success') {
				message = Liferay.Language.get('your-request-processed-successfully');
				messageClass = 'portlet-msg-success';
			}
			else {
				message = Liferay.Language.get('your-settings-could-not-be-saved');
				messageClass = 'portlet-msg-error';
			}

			if (!ajaxResponseMsg.length) {
				ajaxResponse = jQuery(ajaxResponseHTML);
				newPanel.find('form').prepend(ajaxResponse);
				instance._portletMsgResponse = ajaxResponse;
			}

			ajaxResponse.hide();
			ajaxResponse.attr('class', messageClass);
			ajaxResponse.empty();
			ajaxResponse.html(message);
			ajaxResponse.fadeIn('normal');
		};

		instance._saveButton.unbind('click.liferay').bind(
			'click.liferay',
			function() {
				instance._objData.advancedData.customCSS = instance._customCSS.val();

				instance._objData.wapData.title = instance._wapTitleInput.val();
				instance._objData.wapData.initialWindowState = instance._wapInitialWindowStateSelect.val();

				jQuery.ajax(
					{
						url: themeDisplay.getPathMain() + '/portlet_configuration/update_look_and_feel',
						data: {
							p_l_id: themeDisplay.getPlid(),
							doAsUserId: themeDisplay.getDoAsUserIdEncoded(),
							portletId: instance._portletId,
							css: jQuery.toJSON(instance._objData)
						},
						complete: saveHandler,
						type: "POST"
					}
				);
			}
		);

		instance._resetButton.unbind('click.liferay').bind(
			'click.liferay',
			function() {
				instance._curPortlet.attr('style', '');
				jQuery('#lfr-custom-css-block-' + instance._curPortletWrapperId).remove();
				instance._objData = defaultData;
				instance._setDefaults();
			}
		);
	},

	_portletConfig: function() {
		var instance = this;

		var portletData = instance._objData.portletData;
		var customTitleInput = instance._customTitleInput;
		var customTitleCheckbox = instance._customTitleCheckbox;
		var showBorders = instance._showBorders;
		var language = instance._portletLanguage;
		var borderNote = instance._borderNote;
		var portletLinksTarget = instance._portletLinksTarget;

		// Use custom title

		customTitleCheckbox.unbind('click.liferay').bind(
			'click.liferay',
			function() {
				var title;

				portletData.useCustomTitle = this.checked;

				if (this.checked) {
					customTitleInput.attr('disabled', false);
					language.attr('disabled', false);
					title = jQuery.trim(customTitleInput.val());

					if (title == '') {
						title = instance._curPortlet.find('.portlet-title').eq(0).text();
						title = jQuery.trim(title);
						customTitleInput.val(title);
					}

					portletData.title = title;
					instance._portletTitles(false, title);
				}
				else {
					customTitleInput.attr('disabled', true);
					language.attr('disabled', true);
					title = instance._defaultPortletTitle;
				}

				instance._curPortlet.find('.portlet-title').eq(0).text(title);
			}
		);

		customTitleInput.unbind('keyup.liferay').bind(
			'keyup.liferay',
			function() {
				if (!portletData.useCustomTitle || instance._portletLanguage.find('option:selected').val() != instance._currentLanguage) {
					return;
				}

				instance._curPortlet.find('.portlet-title').eq(0).text(this.value);
				portletData.title = this.value;
				instance._portletTitles(false, this.value);
			}
		);

		// Show borders

		showBorders.unbind('click.liferay').bind(
			'click.liferay',
			function() {
				borderNote.toggle();
				portletData.showBorders = this.checked;
			}
		);

		language.bind('change.liferay',
			function() {
				portletData.language = this.options[this.selectedIndex].value;

				var title = instance._portletTitles(portletData.language);

				if (portletData.useCustomTitle) {
					customTitleInput.val(title);
				}
			}
		);

		// Point target links to

		portletLinksTarget.bind('change.liferay',
			function() {
				portletData.portletLinksTarget = instance._getSafeInteger(this.options[this.selectedIndex].value);
			}
		);
	},

	_portletTitles: function(key, value) {
		var instance = this;

		var portletLanguage = instance._portletLanguage;

		if (!instance._objData.portletData.titles) {
			instance._objData.portletData.titles = {};
		}

		var portletTitles = instance._objData.portletData.titles;

		if (!key) {
			key = instance._portletLanguage.find('option:selected').val();
		}

		if (value == null) {
			var portletTitle = portletTitles[key];

			if (portletTitle) {
				return portletTitle;
			}

			return '';
		}
		else {
			portletTitles[key] = value;

			if (value == '') {
				instance._languageClasses(key, null, true);
			}
			else {
				instance._languageClasses(key);
			}
		}
	},

	_setCheckbox: function(obj, value) {
		var instance = this;

		obj.attr('checked', value);
	},

	_setDefaults: function() {
		var instance = this;

		var objData = instance._objData;

		var portletData = objData.portletData;
		var textData = objData.textData;
		var bgData = objData.bgData;
		var borderData = objData.borderData;
		var spacingData = objData.spacingData;
		var wapData = objData.wapData;

		if (wapData == null) {
			wapData = {
				title: '',
				initialWindowState: 'NORMAL'
			};

			objData.wapData = wapData;
		}

		var portletTitles = portletData.titles;
		var portletTitle = instance._portletTitles(portletData.language);

		var fontStyle = false;
		var fontWeight = false;

		if (textData.fontStyle != 'normal') {
			fontStyle = true;
		}

		if (textData.fontWeight != 'normal') {
			fontWeight = true;
		}

		// Portlet config

		instance._setInput(instance._customTitleInput, portletTitle);
		instance._setCheckbox(instance._customTitleCheckbox, portletData.useCustomTitle);
		instance._setCheckbox(instance._showBorders, portletData.showBorders);
		instance._setSelect(instance._portletLanguage, instance._currentLanguage);
		instance._setSelect(instance._portletLinksTarget, portletData.portletLinksTarget);

		if (!portletData.useCustomTitle) {
			instance._customTitleInput.attr('disabled', true);
			instance._portletLanguage.attr('disabled', true);
		}

		if (portletData.titles) {
			jQuery.each(portletData.titles,
				function(key, value) {
					instance._languageClasses(key);
				}
			);
		}

		// Text

		instance._setSelect(instance._fontFamily, textData.fontFamily);
		instance._setCheckbox(instance._fontWeight, fontWeight);
		instance._setCheckbox(instance._fontStyle, fontStyle);
		instance._setSelect(instance._fontSize, textData.fontSize);
		instance._setInput(instance._fontColor, textData.color);
		instance._setSelect(instance._textAlign, textData.textAlign);
		instance._setSelect(instance._textDecoration, textData.textDecoration);
		instance._setSelect(instance._wordSpacing, textData.wordSpacing);
		instance._setSelect(instance._leading, textData.lineHeight);
		instance._setSelect(instance._tracking, textData.letterSpacing);

		// Background

		instance._setInput(instance._backgroundColor, bgData.backgroundColor);
		instance._setCheckbox(instance._useBgImage, bgData.useBgImage);
		instance._setSelect(instance._bgRepeating, bgData.backgroundRepeat);
		instance._setInput(instance._bgPosTop, bgData.backgroundPosition.top.value);
		instance._setSelect(instance._bgPosTopUnit, bgData.backgroundPosition.top.unit);
		instance._setInput(instance._bgPosLeft, bgData.backgroundPosition.left.value);
		instance._setSelect(instance._bgPosLeftUnit, bgData.backgroundPosition.left.unit);

		// Border

		instance._setCheckbox(instance._ufaBorderWidth, borderData.borderWidth.sameForAll);
		instance._setCheckbox(instance._ufaBorderStyle, borderData.borderStyle.sameForAll);
		instance._setCheckbox(instance._ufaBorderColor, borderData.borderColor.sameForAll);

		instance._setInput(instance._borderTopInt, borderData.borderWidth.top.value);
		instance._setSelect(instance._borderTopUnit, borderData.borderWidth.top.unit);
		instance._setInput(instance._borderRightInt, borderData.borderWidth.right.value);
		instance._setSelect(instance._borderRightUnit, borderData.borderWidth.right.unit);
		instance._setInput(instance._borderBottomInt, borderData.borderWidth.bottom.value);
		instance._setSelect(instance._borderBottomUnit, borderData.borderWidth.bottom.unit);
		instance._setInput(instance._borderLeftInt, borderData.borderWidth.left.value);
		instance._setSelect(instance._borderLeftUnit, borderData.borderWidth.left.unit);

		instance._setSelect(instance._borderTopStyle, borderData.borderStyle.top);
		instance._setSelect(instance._borderRightStyle, borderData.borderStyle.right);
		instance._setSelect(instance._borderBottomStyle, borderData.borderStyle.bottom);
		instance._setSelect(instance._borderLeftStyle, borderData.borderStyle.left);

		instance._setInput(instance._borderTopColor, borderData.borderColor.top);
		instance._setInput(instance._borderRightColor, borderData.borderColor.right);
		instance._setInput(instance._borderBottomColor, borderData.borderColor.bottom);
		instance._setInput(instance._borderLeftColor, borderData.borderColor.left);

		// Spacing

		instance._setCheckbox(instance._ufaPadding, spacingData.padding.sameForAll);
		instance._setCheckbox(instance._ufaMargin, spacingData.margin.sameForAll);

		instance._setInput(instance._paddingTopInt, spacingData.padding.top.value);
		instance._setSelect(instance._paddingTopUnit, spacingData.padding.top.unit);
		instance._setInput(instance._paddingRightInt, spacingData.padding.right.value);
		instance._setSelect(instance._paddingRightUnit, spacingData.padding.right.unit);
		instance._setInput(instance._paddingBottomInt, spacingData.padding.bottom.value);
		instance._setSelect(instance._paddingBottomUnit, spacingData.padding.bottom.unit);
		instance._setInput(instance._paddingLeftInt, spacingData.padding.left.value);
		instance._setSelect(instance._paddingLeftUnit, spacingData.padding.left.unit);

		instance._setInput(instance._marginTopInt, spacingData.margin.top.value);
		instance._setSelect(instance._marginTopUnit, spacingData.margin.top.unit);
		instance._setInput(instance._marginRightInt, spacingData.margin.right.value);
		instance._setSelect(instance._marginRightUnit, spacingData.margin.right.unit);
		instance._setInput(instance._marginBottomInt, spacingData.margin.bottom.value);
		instance._setSelect(instance._marginBottomUnit, spacingData.margin.bottom.unit);
		instance._setInput(instance._marginLeftInt, spacingData.margin.left.value);
		instance._setSelect(instance._marginLeftUnit, spacingData.margin.left.unit);

		// Advanced CSS

		var customStyleBlock = jQuery('#lfr-custom-css-block-' + instance._curPortletWrapperId);

		var customStyles = customStyleBlock.html();

		if (customStyles == '' || customStyles == null) {
			customStyles = objData.advancedData.customCSS;
		}

		instance._setTextarea(instance._customCSS, customStyles);

		// WAP styling

		instance._setInput(instance._wapTitleInput, wapData.title);
		instance._setSelect(instance._wapInitialWindowStateSelect, wapData.initialWindowState);
	},

	_setInput: function(obj, value) {
		var instance = this;

		obj.val(value);
	},

	_setSelect: function(obj, value) {
		var instance = this;

		obj.find('option[value=' + value + ']').attr('selected', 'selected');
	},

	_setTextarea: function(obj, value) {
		var instance = this;

		instance._setInput(obj, value);
	},

	_spacingStyles: function() {
		var instance = this;

		var portlet = instance._curPortlet;

		var ufaPadding = instance._ufaPadding;
		var ufaMargin = instance._ufaMargin;

		var spacingData = instance._objData.spacingData;

		// Padding

		var pTop = instance._paddingTopInt;
		var pTopUnit = instance._paddingTopUnit;
		var pRight = instance._paddingRightInt;
		var pRightUnit = instance._paddingRightUnit;
		var pBottom = instance._paddingBottomInt;
		var pBottomUnit = instance._paddingBottomUnit;
		var pLeft = instance._paddingLeftInt;
		var pLeftUnit = instance._paddingLeftUnit;

		var changePadding = function() {
			var styling = {};

			var padding = instance._getCombo(pTop, pTopUnit);

			styling = {padding: padding.both};

			var ufa = ufaPadding.is(':checked');

			spacingData.padding.top.value = padding.input;
			spacingData.padding.top.unit = padding.selectBox;

			spacingData.padding.sameForAll = ufa;

			if (!ufa) {
				var extStyling = {};

				extStyling.paddingTop = styling.padding;

				var right = instance._getCombo(pRight, pRightUnit);
				var bottom = instance._getCombo(pBottom, pBottomUnit);
				var left = instance._getCombo(pLeft, pLeftUnit);

				extStyling.paddingRight = right.both;
				extStyling.paddingBottom = bottom.both;
				extStyling.paddingLeft = left.both;

				styling = extStyling;

				spacingData.padding.right.value = right.input;
				spacingData.padding.right.unit = right.selectBox;

				spacingData.padding.bottom.value = bottom.input;
				spacingData.padding.bottom.unit = bottom.selectBox;

				spacingData.padding.left.value = left.input;
				spacingData.padding.left.unit = left.selectBox;
			}

			portlet.css(styling);
		};

		pTop.unbind('blur.liferay').bind('blur.liferay', changePadding);
		pRight.unbind('blur.liferay').bind('blur.liferay', changePadding);
		pBottom.unbind('blur.liferay').bind('blur.liferay', changePadding);
		pLeft.unbind('blur.liferay').bind('blur.liferay', changePadding);

		pTop.unbind('keyup.liferay').bind('keyup.liferay', changePadding);
		pRight.unbind('keyup.liferay').bind('keyup.liferay', changePadding);
		pBottom.unbind('keyup.liferay').bind('keyup.liferay', changePadding);
		pLeft.unbind('keyup.liferay').bind('keyup.liferay', changePadding);

		pTopUnit.unbind('change.liferay').bind('change.liferay', changePadding);
		pRightUnit.unbind('change.liferay').bind('change.liferay', changePadding);
		pBottomUnit.unbind('change.liferay').bind('change.liferay', changePadding);
		pLeftUnit.unbind('change.liferay').bind('change.liferay', changePadding);

		ufaPadding.unbind('click.liferay').bind('click.liferay', changePadding);

		// Margin

		var mTop = instance._marginTopInt;
		var mTopUnit = instance._marginTopUnit;
		var mRight = instance._marginRightInt;
		var mRightUnit = instance._marginRightUnit;
		var mBottom = instance._marginBottomInt;
		var mBottomUnit = instance._marginBottomUnit;
		var mLeft = instance._marginLeftInt;
		var mLeftUnit = instance._marginLeftUnit;

		var changeMargin = function() {
			var styling = {};

			var margin = instance._getCombo(mTop, mTopUnit);

			styling = {margin: margin.both};

			var ufa = ufaMargin.is(':checked');

			spacingData.margin.top.value = margin.input;
			spacingData.margin.top.unit = margin.selectBox;

			spacingData.margin.sameForAll = ufa;

			if (!ufa) {
				var extStyling = {};

				extStyling.marginTop = styling.margin;

				var right = instance._getCombo(mRight, mRightUnit);
				var bottom = instance._getCombo(mBottom, mBottomUnit);
				var left = instance._getCombo(mLeft, mLeftUnit);

				extStyling.marginRight = right.both;
				extStyling.marginBottom = bottom.both;
				extStyling.marginLeft = left.both;

				styling = extStyling;

				spacingData.margin.right.value = right.input;
				spacingData.margin.right.unit = right.selectBox;

				spacingData.margin.bottom.value = bottom.input;
				spacingData.margin.bottom.unit = bottom.selectBox;

				spacingData.margin.left.value = left.input;
				spacingData.margin.left.unit = left.selectBox;
			}

			portlet.css(styling);
		};

		mTop.unbind('blur.liferay').bind('blur.liferay', changeMargin);
		mRight.unbind('blur.liferay').bind('blur.liferay', changeMargin);
		mBottom.unbind('blur.liferay').bind('blur.liferay', changeMargin);
		mLeft.unbind('blur.liferay').bind('blur.liferay', changeMargin);

		mTop.unbind('keyup.liferay').bind('keyup.liferay', changeMargin);
		mRight.unbind('keyup.liferay').bind('keyup.liferay', changeMargin);
		mBottom.unbind('keyup.liferay').bind('keyup.liferay', changeMargin);
		mLeft.unbind('keyup.liferay').bind('keyup.liferay', changeMargin);

		mTopUnit.unbind('change.liferay').bind('change.liferay', changeMargin);
		mRightUnit.unbind('change.liferay').bind('change.liferay', changeMargin);
		mBottomUnit.unbind('change.liferay').bind('change.liferay', changeMargin);
		mLeftUnit.unbind('change.liferay').bind('change.liferay', changeMargin);

		ufaMargin.unbind('click.liferay').bind('click.liferay', changeMargin);
	},

	_textStyles: function() {
		var instance = this;

		var portlet = instance._curPortlet;
		var fontFamily = instance._fontFamily;
		var fontBold = instance._fontWeight;
		var fontItalic = instance._fontStyle;
		var fontSize = instance._fontSize;
		var fontColor = instance._fontColor;
		var textAlign = instance._textAlign;
		var textDecoration = instance._textDecoration;
		var wordSpacing = instance._wordSpacing;
		var leading = instance._leading;
		var tracking = instance._tracking;

		var textData = instance._objData.textData;

		// Font family

		fontFamily.unbind('change.liferay').bind(
			'change.liferay',
			function() {
				var fontFamily = this.options[this.selectedIndex].value;

				portlet.css('font-family', fontFamily);

				textData.fontFamily = fontFamily;
			}
		);

		// Font style

		fontBold.unbind('click.liferay').bind(
			'click.liferay',
			function() {
			var style = 'normal';
				if (this.checked) {
					style = 'bold';
				}

				portlet.css('font-weight', style);

				textData.fontWeight = style;
			}
		);

		fontItalic.unbind('click.liferay').bind(
			'click.liferay',
			function() {
				var style = 'normal';

				if (this.checked) {
					style = 'italic';
				}

				portlet.css('font-style', style);

				textData.fontStyle = style;
			}
		);

		// Font size

		fontSize.unbind('change.liferay').bind(
			'change.liferay',
			function() {
				var fontSize = this.options[this.selectedIndex].value;

				portlet.css('font-size', fontSize);

				textData.fontSize = fontSize;
			}
		);

		// Font color

		var setColor = function(obj) {
			var color = obj.value;

			if (color != '') {
				portlet.css('color', color);

				textData.color = color;
			}
		};

		var colorPicker = new Expanse.ColorPickerPanel(
			{
				buttonContext: jQuery('#portlet-set-properties')[0],
				item: fontColor[0],
				on: {
					change: function() {
						setColor(fontColor[0]);
					}
				}
			}
		);

		fontColor.unbind('blur.liferay').bind(
			'blur.liferay',
			function() {
				setColor(this);
			}
		);

		// Text alignment

		textAlign.unbind('change.liferay').bind(
			'change.liferay',
			function() {
				var textAlign = this.options[this.selectedIndex].value;

				portlet.css('text-align', textAlign);

				textData.textAlign = textAlign;
			}
		);

		// Text decoration

		textDecoration.unbind('change.liferay').bind(
			'change.liferay',
			function() {
				var decoration = this.options[this.selectedIndex].value;

				portlet.css('text-decoration', decoration);

				textData.textDecoration = decoration;
			}
		);

		// Word spacing

		wordSpacing.unbind('change.liferay').bind(
			'change.liferay',
			function() {
				var spacing = this.options[this.selectedIndex].value;

				portlet.css('word-spacing', spacing);

				textData.wordSpacing = spacing;
			}
		);

		// Line height

		leading.unbind('change.liferay').bind(
			'change.liferay',
			function() {
				var leading = this.options[this.selectedIndex].value;

				portlet.css('line-height', leading);

				textData.lineHeight = leading;
			}
		);

		// Letter spacing

		tracking.unbind('change.liferay').bind(
			'change.liferay',
			function() {
				var tracking = this.options[this.selectedIndex].value;

				portlet.css('letter-spacing', tracking);

				textData.letterSpacing = tracking;
			}
		);
	}
};