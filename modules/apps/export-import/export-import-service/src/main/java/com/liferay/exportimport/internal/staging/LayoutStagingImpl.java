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

package com.liferay.exportimport.internal.staging;

import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.staging.LayoutStaging;
import com.liferay.exportimport.kernel.staging.LayoutStagingUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutRevision;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.LayoutSetBranch;
import com.liferay.portal.kernel.model.LayoutSetStagingHandler;
import com.liferay.portal.kernel.model.LayoutStagingHandler;
import com.liferay.portal.kernel.service.LayoutRevisionLocalService;
import com.liferay.portal.kernel.service.LayoutSetBranchLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.lang.reflect.InvocationHandler;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Raymond Augé
 */
@Component(immediate = true, service = LayoutStaging.class)
public class LayoutStagingImpl implements LayoutStaging {

	@Override
	public LayoutRevision getLayoutRevision(Layout layout) {
		LayoutStagingHandler layoutStagingHandler = getLayoutStagingHandler(
			layout);

		if (layoutStagingHandler == null) {
			return null;
		}

		return layoutStagingHandler.getLayoutRevision();
	}

	@Override
	public LayoutSetBranch getLayoutSetBranch(LayoutSet layoutSet) {
		LayoutSetStagingHandler layoutSetStagingHandler =
			getLayoutSetStagingHandler(layoutSet);

		if (layoutSetStagingHandler == null) {
			return null;
		}

		return layoutSetStagingHandler.getLayoutSetBranch();
	}

	@Override
	public LayoutSetStagingHandler getLayoutSetStagingHandler(
		LayoutSet layoutSet) {

		if (!ProxyUtil.isProxyClass(layoutSet.getClass())) {
			return null;
		}

		InvocationHandler invocationHandler = ProxyUtil.getInvocationHandler(
			layoutSet);

		if (!(invocationHandler instanceof LayoutSetStagingHandler)) {
			return null;
		}

		return (LayoutSetStagingHandler)invocationHandler;
	}

	@Override
	public LayoutStagingHandler getLayoutStagingHandler(Layout layout) {
		if (layout == null) {
			return null;
		}

		if (!ProxyUtil.isProxyClass(layout.getClass())) {
			return null;
		}

		InvocationHandler invocationHandler = ProxyUtil.getInvocationHandler(
			layout);

		if (!(invocationHandler instanceof LayoutStagingHandler)) {
			return null;
		}

		return (LayoutStagingHandler)invocationHandler;
	}

	@Override
	public boolean isBranchingLayout(Layout layout) {
		if ((layout == null) || layout.isSystem()) {
			return false;
		}

		return isBranchingLayoutSet(
			layout.getGroup(), layout.isPrivateLayout());
	}

	@Override
	public boolean isBranchingLayoutSet(Group group, boolean privateLayout) {
		boolean isStagingGroup = false;

		if (group.isStagingGroup() && !group.isStagedRemotely()) {
			isStagingGroup = true;

			group = group.getLiveGroup();
		}

		UnicodeProperties typeSettingsUnicodeProperties =
			group.getTypeSettingsProperties();

		if (typeSettingsUnicodeProperties.isEmpty()) {
			return false;
		}

		boolean branchingEnabled = false;

		if (privateLayout) {
			branchingEnabled = GetterUtil.getBoolean(
				typeSettingsUnicodeProperties.getProperty("branchingPrivate"));
		}
		else {
			branchingEnabled = GetterUtil.getBoolean(
				typeSettingsUnicodeProperties.getProperty("branchingPublic"));
		}

		if (!branchingEnabled || !group.isStaged() ||
			(!group.isStagedRemotely() && !isStagingGroup)) {

			return false;
		}

		Group stagingGroup = group;

		if (isStagingGroup) {
			stagingGroup = group.getStagingGroup();
		}

		try {
			_layoutSetBranchLocalService.getMasterLayoutSetBranch(
				stagingGroup.getGroupId(), privateLayout);

			return true;
		}
		catch (PortalException portalException) {

			// LPS-52675

			if (_log.isDebugEnabled()) {
				_log.debug(portalException, portalException);
			}

			return false;
		}
	}

	@Override
	public Layout mergeLayoutRevisionIntoLayout(Layout layout) {
		LayoutStagingHandler layoutStagingHandler = getLayoutStagingHandler(
			layout);

		if (layoutStagingHandler == null) {
			return (Layout)layout.clone();
		}

		layout = layoutStagingHandler.getLayout();
		layout = (Layout)layout.clone();

		LayoutRevision layoutRevision =
			layoutStagingHandler.getLayoutRevision();

		layout.setName(layoutRevision.getName());
		layout.setTitle(layoutRevision.getTitle());
		layout.setDescription(layoutRevision.getDescription());
		layout.setKeywords(layoutRevision.getKeywords());
		layout.setRobots(layoutRevision.getRobots());
		layout.setTypeSettings(layoutRevision.getTypeSettings());
		layout.setIconImageId(layoutRevision.getIconImageId());
		layout.setThemeId(layoutRevision.getThemeId());
		layout.setColorSchemeId(layoutRevision.getColorSchemeId());
		layout.setCss(layoutRevision.getCss());

		return layout;
	}

	@Override
	public LayoutSet mergeLayoutSetRevisionIntoLayoutSet(LayoutSet layoutSet) {
		LayoutSetStagingHandler layoutSetStagingHandler =
			getLayoutSetStagingHandler(layoutSet);

		if (layoutSetStagingHandler == null) {
			return (LayoutSet)layoutSet.clone();
		}

		layoutSet = layoutSetStagingHandler.getLayoutSet();
		layoutSet = (LayoutSet)layoutSet.clone();

		LayoutSetBranch layoutSetBranch =
			layoutSetStagingHandler.getLayoutSetBranch();

		layoutSet.setLogoId(layoutSetBranch.getLogoId());
		layoutSet.setThemeId(layoutSetBranch.getThemeId());
		layoutSet.setColorSchemeId(layoutSetBranch.getColorSchemeId());
		layoutSet.setCss(layoutSetBranch.getCss());
		layoutSet.setSettings(layoutSetBranch.getSettings());
		layoutSet.setLayoutSetPrototypeUuid(
			layoutSetBranch.getLayoutSetPrototypeUuid());
		layoutSet.setLayoutSetPrototypeLinkEnabled(
			layoutSetBranch.isLayoutSetPrototypeLinkEnabled());

		return layoutSet;
	}

	@Override
	public boolean prepareLayoutStagingHandler(
		PortletDataContext portletDataContext, Layout layout) {

		boolean exportLAR = MapUtil.getBoolean(
			portletDataContext.getParameterMap(), "exportLAR");

		if (exportLAR || !LayoutStagingUtil.isBranchingLayout(layout)) {
			return true;
		}

		long layoutSetBranchId = MapUtil.getLong(
			portletDataContext.getParameterMap(), "layoutSetBranchId");

		if (layoutSetBranchId <= 0) {
			return false;
		}

		LayoutRevision layoutRevision = null;

		List<LayoutRevision> layoutRevisions =
			_layoutRevisionLocalService.getLayoutRevisions(
				layoutSetBranchId, layout.getPlid(), true);

		if (!layoutRevisions.isEmpty()) {
			if (layoutRevisions.size() > 1) {
				layoutRevision = getLayoutRevision(layout);

				long layoutBranchId = GetterUtil.DEFAULT_LONG;

				if (layoutRevision != null) {
					layoutBranchId = layoutRevision.getLayoutBranchId();
				}

				layoutRevision =
					_layoutRevisionLocalService.fetchLayoutRevision(
						layoutSetBranchId, layoutBranchId, true,
						layout.getPlid());
			}

			if ((layoutRevision == null) && !layoutRevisions.isEmpty()) {
				layoutRevision = layoutRevisions.get(0);
			}
		}

		if (layoutRevision == null) {
			return false;
		}

		LayoutStagingHandler layoutStagingHandler =
			LayoutStagingUtil.getLayoutStagingHandler(layout);

		layoutStagingHandler.setLayoutRevision(layoutRevision);

		return true;
	}

	@Reference(unbind = "-")
	protected void setLayoutSetBranchLocalService(
		LayoutSetBranchLocalService layoutSetBranchLocalService) {

		_layoutSetBranchLocalService = layoutSetBranchLocalService;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutStagingImpl.class);

	@Reference
	private LayoutRevisionLocalService _layoutRevisionLocalService;

	private LayoutSetBranchLocalService _layoutSetBranchLocalService;

}