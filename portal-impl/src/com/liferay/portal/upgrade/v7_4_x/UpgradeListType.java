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

package com.liferay.portal.upgrade.v7_4_x;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Address;
import com.liferay.portal.kernel.model.ListType;
import com.liferay.portal.kernel.model.ListTypeConstants;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;

import java.sql.PreparedStatement;

/**
 * @author Pei-Jung Lan
 */
public class UpgradeListType extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		_addListType("phone-number", _ADDRESS_PHONE_LIST_TYPE_NAME);
	}

	private void _addListType(String name, String type) {
		StringBundler sb = new StringBundler(7);

		sb.append("insert into ListType (listTypeId, name, type_) select ?, ");
		sb.append("?, ? where not exists (select null from ListType where ");
		sb.append("name = ");
		sb.append(StringUtil.quote(name));
		sb.append("and type_ = ");
		sb.append(StringUtil.quote(type));
		sb.append(")");

		String sql = sb.toString();

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setLong(1, increment(ListType.class.getName()));
			ps.setString(2, name);
			ps.setString(3, type);

			ps.executeUpdate();
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to add list type", exception);
			}
		}
	}

	private static final String _ADDRESS_PHONE_LIST_TYPE_NAME =
		Address.class.getName() + ListTypeConstants.PHONE;

	private static final Log _log = LogFactoryUtil.getLog(
		UpgradeListType.class);

}