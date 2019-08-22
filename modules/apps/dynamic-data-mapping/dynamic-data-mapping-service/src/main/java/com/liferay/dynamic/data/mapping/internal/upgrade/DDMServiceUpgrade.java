/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.dynamic.data.mapping.internal.upgrade;

import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.document.library.kernel.service.DLFileVersionLocalService;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
import com.liferay.document.library.kernel.store.Store;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderTracker;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.internal.upgrade.v1_0_0.UpgradeCompanyId;
import com.liferay.dynamic.data.mapping.internal.upgrade.v1_0_0.UpgradeKernelPackage;
import com.liferay.dynamic.data.mapping.internal.upgrade.v1_0_0.UpgradeLastPublishDate;
import com.liferay.dynamic.data.mapping.internal.upgrade.v1_0_0.UpgradeSchema;
import com.liferay.dynamic.data.mapping.internal.upgrade.v1_0_1.UpgradeResourcePermission;
import com.liferay.dynamic.data.mapping.internal.upgrade.v1_0_2.UpgradeDDMTemplateSmallImageURL;
import com.liferay.dynamic.data.mapping.internal.upgrade.v1_1_0.UpgradeCheckboxFieldToCheckboxMultipleField;
import com.liferay.dynamic.data.mapping.internal.upgrade.v1_1_1.UpgradeDDMFormFieldSettings;
import com.liferay.dynamic.data.mapping.internal.upgrade.v2_0_3.UpgradeDDMFormInstanceDefinition;
import com.liferay.dynamic.data.mapping.internal.upgrade.v2_0_3.UpgradeDDMFormInstanceEntries;
import com.liferay.dynamic.data.mapping.io.DDMFormJSONDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormJSONSerializer;
import com.liferay.dynamic.data.mapping.io.DDMFormLayoutJSONSerializer;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONSerializer;
import com.liferay.dynamic.data.mapping.io.DDMFormXSDDeserializer;
import com.liferay.dynamic.data.mapping.util.DDM;
import com.liferay.expando.kernel.service.ExpandoRowLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.expando.kernel.service.ExpandoValueLocalService;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.security.permission.ResourceActions;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.ResourceActionLocalService;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.upgrade.DummyUpgradeStep;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	service = {DDMServiceUpgrade.class, UpgradeStepRegistrator.class}
)
public class DDMServiceUpgrade implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register("0.0.1", "0.0.2", new UpgradeSchema());

		registry.register("0.0.2", "0.0.3", new UpgradeKernelPackage());

		registry.register(
			"0.0.3", "1.0.0", new UpgradeCompanyId(),
			new com.liferay.dynamic.data.mapping.internal.upgrade.v1_0_0.
				UpgradeDynamicDataMapping(
					_assetEntryLocalService, _ddm, _ddmFormJSONDeserializer,
					_ddmFormJSONSerializer, _ddmFormLayoutJSONSerializer,
					_ddmFormValuesJSONDeserializer,
					_ddmFormValuesJSONSerializer, _ddmFormXSDDeserializer,
					_dlFileEntryLocalService, _dlFileVersionLocalService,
					_dlFolderLocalService, _expandoRowLocalService,
					_expandoTableLocalService, _expandoValueLocalService,
					_resourceActions, _resourceLocalService,
					_resourcePermissionLocalService, _store),
			new UpgradeLastPublishDate());

		registry.register(
			"1.0.0", "1.0.1", new UpgradeResourcePermission(_resourceActions));

		registry.register(
			"1.0.1", "1.0.2", new UpgradeDDMTemplateSmallImageURL());

		registry.register(
			"1.0.2", "1.0.3",
			new com.liferay.dynamic.data.mapping.internal.upgrade.v1_0_3.
				UpgradeDDMFormParagraphFields(_jsonFactory));

		registry.register(
			"1.0.3", "1.1.0",
			new UpgradeCheckboxFieldToCheckboxMultipleField(
				_ddmFormJSONDeserializer, _ddmFormValuesJSONDeserializer,
				_ddmFormValuesJSONSerializer, _jsonFactory),
			new com.liferay.dynamic.data.mapping.internal.upgrade.v1_1_0.
				UpgradeDDMStructure(
					_ddmExpressionFactory, _ddmFormJSONDeserializer,
					_ddmFormJSONSerializer),
			new com.liferay.dynamic.data.mapping.internal.upgrade.v1_1_0.
				UpgradeDataProviderInstance(_jsonFactory));

		registry.register(
			"1.1.0", "1.1.1",
			new UpgradeDDMFormFieldSettings(
				_ddmFormJSONDeserializer, _ddmFormJSONSerializer),
			new com.liferay.dynamic.data.mapping.internal.upgrade.v1_1_1.
				UpgradeDataProviderInstance(
					_ddmDataProviderTracker, _ddmFormValuesJSONDeserializer,
					_ddmFormValuesJSONSerializer));

		registry.register(
			"1.1.1", "1.1.2",
			new com.liferay.dynamic.data.mapping.internal.upgrade.v1_1_2.
				UpgradeDynamicDataMapping(
					_ddmFormJSONDeserializer, _ddmFormJSONSerializer,
					_ddmFormValuesJSONDeserializer,
					_ddmFormValuesJSONSerializer, _jsonFactory));

		registry.register(
			"1.1.2", "1.1.3",
			new com.liferay.dynamic.data.mapping.internal.upgrade.v1_1_3.
				UpgradeDDMStorageLink());

		registry.register(
			"1.1.3", "1.2.0",
			new com.liferay.dynamic.data.mapping.internal.upgrade.v1_2_0.
				UpgradeSchema());

		registry.register("1.2.0", "1.2.1", new DummyUpgradeStep());

		registry.register(
			"1.2.1", "2.0.0",
			new com.liferay.dynamic.data.mapping.internal.upgrade.v2_0_0.
				UpgradeDDMFormInstance(
					_classNameLocalService, _counterLocalService,
					_resourceActions, _resourceActionLocalService,
					_resourcePermissionLocalService),
			new com.liferay.dynamic.data.mapping.internal.upgrade.v2_0_0.
				UpgradeDDMFormInstanceRecord(_assetEntryLocalService),
			new com.liferay.dynamic.data.mapping.internal.upgrade.v2_0_0.
				UpgradeDDMFormInstanceRecordVersion(),
			new com.liferay.dynamic.data.mapping.internal.upgrade.v2_0_0.
				UpgradeResourceAction(_resourceActionLocalService));

		registry.register(
			"2.0.0", "2.0.1",
			new com.liferay.dynamic.data.mapping.internal.upgrade.v2_0_1.
				UpgradeAutocompleteDDMTextFieldSetting(
					_ddmFormJSONDeserializer, _ddmFormJSONSerializer));

		registry.register(
			"2.0.1", "2.0.2",
			new com.liferay.dynamic.data.mapping.internal.upgrade.v2_0_2.
				UpgradeDDMFormInstanceStructureResourceAction(
					_resourceActions));

		registry.register(
			"2.0.2", "2.0.3",
			new com.liferay.dynamic.data.mapping.internal.upgrade.v2_0_3.
				UpgradeDataProviderInstance(_jsonFactory),
			new UpgradeDDMFormInstanceDefinition(_jsonFactory),
			new UpgradeDDMFormInstanceEntries(_jsonFactory),
			new com.liferay.dynamic.data.mapping.internal.upgrade.v2_0_3.
				UpgradeDDMFormInstanceSettings(_jsonFactory));

		registry.register(
			"2.0.3", "2.0.4",
			new com.liferay.dynamic.data.mapping.internal.upgrade.v2_0_4.
				UpgradeDDMFormParagraphFields(_jsonFactory));

		registry.register(
			"2.0.4", "2.0.5",
			new com.liferay.dynamic.data.mapping.internal.upgrade.v2_0_5.
				UpgradeDDMFormFieldValidation(_jsonFactory));
	}

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private CounterLocalService _counterLocalService;

	@Reference
	private DDM _ddm;

	@Reference
	private DDMDataProviderTracker _ddmDataProviderTracker;

	@Reference
	private DDMExpressionFactory _ddmExpressionFactory;

	@Reference
	private DDMFormJSONDeserializer _ddmFormJSONDeserializer;

	@Reference
	private DDMFormJSONSerializer _ddmFormJSONSerializer;

	@Reference
	private DDMFormLayoutJSONSerializer _ddmFormLayoutJSONSerializer;

	@Reference
	private DDMFormValuesJSONDeserializer _ddmFormValuesJSONDeserializer;

	@Reference
	private DDMFormValuesJSONSerializer _ddmFormValuesJSONSerializer;

	@Reference
	private DDMFormXSDDeserializer _ddmFormXSDDeserializer;

	@Reference
	private DLFileEntryLocalService _dlFileEntryLocalService;

	@Reference
	private DLFileVersionLocalService _dlFileVersionLocalService;

	@Reference
	private DLFolderLocalService _dlFolderLocalService;

	@Reference
	private ExpandoRowLocalService _expandoRowLocalService;

	@Reference
	private ExpandoTableLocalService _expandoTableLocalService;

	@Reference
	private ExpandoValueLocalService _expandoValueLocalService;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private ResourceActionLocalService _resourceActionLocalService;

	@Reference
	private ResourceActions _resourceActions;

	@Reference
	private ResourceLocalService _resourceLocalService;

	@Reference
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Reference(target = "(dl.store.upgrade=true)")
	private Store _store;

}