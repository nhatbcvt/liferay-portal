/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.bom.service.persistence.impl;

import com.liferay.commerce.bom.exception.NoSuchBOMDefinitionException;
import com.liferay.commerce.bom.model.CommerceBOMDefinition;
import com.liferay.commerce.bom.model.CommerceBOMDefinitionTable;
import com.liferay.commerce.bom.model.impl.CommerceBOMDefinitionImpl;
import com.liferay.commerce.bom.model.impl.CommerceBOMDefinitionModelImpl;
import com.liferay.commerce.bom.service.persistence.CommerceBOMDefinitionPersistence;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.orm.ArgumentsResolver;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.permission.InlineSQLHelperUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

/**
 * The persistence implementation for the commerce bom definition service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Luca Pellizzon
 * @generated
 */
public class CommerceBOMDefinitionPersistenceImpl
	extends BasePersistenceImpl<CommerceBOMDefinition>
	implements CommerceBOMDefinitionPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>CommerceBOMDefinitionUtil</code> to access the commerce bom definition persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		CommerceBOMDefinitionImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByCommerceBOMFolderId;
	private FinderPath _finderPathWithoutPaginationFindByCommerceBOMFolderId;
	private FinderPath _finderPathCountByCommerceBOMFolderId;

	/**
	 * Returns all the commerce bom definitions where commerceBOMFolderId = &#63;.
	 *
	 * @param commerceBOMFolderId the commerce bom folder ID
	 * @return the matching commerce bom definitions
	 */
	@Override
	public List<CommerceBOMDefinition> findByCommerceBOMFolderId(
		long commerceBOMFolderId) {

		return findByCommerceBOMFolderId(
			commerceBOMFolderId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce bom definitions where commerceBOMFolderId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceBOMDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param commerceBOMFolderId the commerce bom folder ID
	 * @param start the lower bound of the range of commerce bom definitions
	 * @param end the upper bound of the range of commerce bom definitions (not inclusive)
	 * @return the range of matching commerce bom definitions
	 */
	@Override
	public List<CommerceBOMDefinition> findByCommerceBOMFolderId(
		long commerceBOMFolderId, int start, int end) {

		return findByCommerceBOMFolderId(commerceBOMFolderId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce bom definitions where commerceBOMFolderId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceBOMDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param commerceBOMFolderId the commerce bom folder ID
	 * @param start the lower bound of the range of commerce bom definitions
	 * @param end the upper bound of the range of commerce bom definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce bom definitions
	 */
	@Override
	public List<CommerceBOMDefinition> findByCommerceBOMFolderId(
		long commerceBOMFolderId, int start, int end,
		OrderByComparator<CommerceBOMDefinition> orderByComparator) {

		return findByCommerceBOMFolderId(
			commerceBOMFolderId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce bom definitions where commerceBOMFolderId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceBOMDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param commerceBOMFolderId the commerce bom folder ID
	 * @param start the lower bound of the range of commerce bom definitions
	 * @param end the upper bound of the range of commerce bom definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce bom definitions
	 */
	@Override
	public List<CommerceBOMDefinition> findByCommerceBOMFolderId(
		long commerceBOMFolderId, int start, int end,
		OrderByComparator<CommerceBOMDefinition> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath =
					_finderPathWithoutPaginationFindByCommerceBOMFolderId;
				finderArgs = new Object[] {commerceBOMFolderId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCommerceBOMFolderId;
			finderArgs = new Object[] {
				commerceBOMFolderId, start, end, orderByComparator
			};
		}

		List<CommerceBOMDefinition> list = null;

		if (useFinderCache) {
			list = (List<CommerceBOMDefinition>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommerceBOMDefinition commerceBOMDefinition : list) {
					if (commerceBOMFolderId !=
							commerceBOMDefinition.getCommerceBOMFolderId()) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_COMMERCEBOMDEFINITION_WHERE);

			sb.append(_FINDER_COLUMN_COMMERCEBOMFOLDERID_COMMERCEBOMFOLDERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommerceBOMDefinitionModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commerceBOMFolderId);

				list = (List<CommerceBOMDefinition>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first commerce bom definition in the ordered set where commerceBOMFolderId = &#63;.
	 *
	 * @param commerceBOMFolderId the commerce bom folder ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce bom definition
	 * @throws NoSuchBOMDefinitionException if a matching commerce bom definition could not be found
	 */
	@Override
	public CommerceBOMDefinition findByCommerceBOMFolderId_First(
			long commerceBOMFolderId,
			OrderByComparator<CommerceBOMDefinition> orderByComparator)
		throws NoSuchBOMDefinitionException {

		CommerceBOMDefinition commerceBOMDefinition =
			fetchByCommerceBOMFolderId_First(
				commerceBOMFolderId, orderByComparator);

		if (commerceBOMDefinition != null) {
			return commerceBOMDefinition;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("commerceBOMFolderId=");
		sb.append(commerceBOMFolderId);

		sb.append("}");

		throw new NoSuchBOMDefinitionException(sb.toString());
	}

	/**
	 * Returns the first commerce bom definition in the ordered set where commerceBOMFolderId = &#63;.
	 *
	 * @param commerceBOMFolderId the commerce bom folder ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce bom definition, or <code>null</code> if a matching commerce bom definition could not be found
	 */
	@Override
	public CommerceBOMDefinition fetchByCommerceBOMFolderId_First(
		long commerceBOMFolderId,
		OrderByComparator<CommerceBOMDefinition> orderByComparator) {

		List<CommerceBOMDefinition> list = findByCommerceBOMFolderId(
			commerceBOMFolderId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce bom definition in the ordered set where commerceBOMFolderId = &#63;.
	 *
	 * @param commerceBOMFolderId the commerce bom folder ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce bom definition
	 * @throws NoSuchBOMDefinitionException if a matching commerce bom definition could not be found
	 */
	@Override
	public CommerceBOMDefinition findByCommerceBOMFolderId_Last(
			long commerceBOMFolderId,
			OrderByComparator<CommerceBOMDefinition> orderByComparator)
		throws NoSuchBOMDefinitionException {

		CommerceBOMDefinition commerceBOMDefinition =
			fetchByCommerceBOMFolderId_Last(
				commerceBOMFolderId, orderByComparator);

		if (commerceBOMDefinition != null) {
			return commerceBOMDefinition;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("commerceBOMFolderId=");
		sb.append(commerceBOMFolderId);

		sb.append("}");

		throw new NoSuchBOMDefinitionException(sb.toString());
	}

	/**
	 * Returns the last commerce bom definition in the ordered set where commerceBOMFolderId = &#63;.
	 *
	 * @param commerceBOMFolderId the commerce bom folder ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce bom definition, or <code>null</code> if a matching commerce bom definition could not be found
	 */
	@Override
	public CommerceBOMDefinition fetchByCommerceBOMFolderId_Last(
		long commerceBOMFolderId,
		OrderByComparator<CommerceBOMDefinition> orderByComparator) {

		int count = countByCommerceBOMFolderId(commerceBOMFolderId);

		if (count == 0) {
			return null;
		}

		List<CommerceBOMDefinition> list = findByCommerceBOMFolderId(
			commerceBOMFolderId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce bom definitions before and after the current commerce bom definition in the ordered set where commerceBOMFolderId = &#63;.
	 *
	 * @param commerceBOMDefinitionId the primary key of the current commerce bom definition
	 * @param commerceBOMFolderId the commerce bom folder ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce bom definition
	 * @throws NoSuchBOMDefinitionException if a commerce bom definition with the primary key could not be found
	 */
	@Override
	public CommerceBOMDefinition[] findByCommerceBOMFolderId_PrevAndNext(
			long commerceBOMDefinitionId, long commerceBOMFolderId,
			OrderByComparator<CommerceBOMDefinition> orderByComparator)
		throws NoSuchBOMDefinitionException {

		CommerceBOMDefinition commerceBOMDefinition = findByPrimaryKey(
			commerceBOMDefinitionId);

		Session session = null;

		try {
			session = openSession();

			CommerceBOMDefinition[] array = new CommerceBOMDefinitionImpl[3];

			array[0] = getByCommerceBOMFolderId_PrevAndNext(
				session, commerceBOMDefinition, commerceBOMFolderId,
				orderByComparator, true);

			array[1] = commerceBOMDefinition;

			array[2] = getByCommerceBOMFolderId_PrevAndNext(
				session, commerceBOMDefinition, commerceBOMFolderId,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected CommerceBOMDefinition getByCommerceBOMFolderId_PrevAndNext(
		Session session, CommerceBOMDefinition commerceBOMDefinition,
		long commerceBOMFolderId,
		OrderByComparator<CommerceBOMDefinition> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_COMMERCEBOMDEFINITION_WHERE);

		sb.append(_FINDER_COLUMN_COMMERCEBOMFOLDERID_COMMERCEBOMFOLDERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(CommerceBOMDefinitionModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(commerceBOMFolderId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commerceBOMDefinition)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommerceBOMDefinition> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the commerce bom definitions that the user has permission to view where commerceBOMFolderId = &#63;.
	 *
	 * @param commerceBOMFolderId the commerce bom folder ID
	 * @return the matching commerce bom definitions that the user has permission to view
	 */
	@Override
	public List<CommerceBOMDefinition> filterFindByCommerceBOMFolderId(
		long commerceBOMFolderId) {

		return filterFindByCommerceBOMFolderId(
			commerceBOMFolderId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce bom definitions that the user has permission to view where commerceBOMFolderId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceBOMDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param commerceBOMFolderId the commerce bom folder ID
	 * @param start the lower bound of the range of commerce bom definitions
	 * @param end the upper bound of the range of commerce bom definitions (not inclusive)
	 * @return the range of matching commerce bom definitions that the user has permission to view
	 */
	@Override
	public List<CommerceBOMDefinition> filterFindByCommerceBOMFolderId(
		long commerceBOMFolderId, int start, int end) {

		return filterFindByCommerceBOMFolderId(
			commerceBOMFolderId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce bom definitions that the user has permissions to view where commerceBOMFolderId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceBOMDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param commerceBOMFolderId the commerce bom folder ID
	 * @param start the lower bound of the range of commerce bom definitions
	 * @param end the upper bound of the range of commerce bom definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce bom definitions that the user has permission to view
	 */
	@Override
	public List<CommerceBOMDefinition> filterFindByCommerceBOMFolderId(
		long commerceBOMFolderId, int start, int end,
		OrderByComparator<CommerceBOMDefinition> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled()) {
			return findByCommerceBOMFolderId(
				commerceBOMFolderId, start, end, orderByComparator);
		}

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				3 + (orderByComparator.getOrderByFields().length * 2));
		}
		else {
			sb = new StringBundler(4);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_COMMERCEBOMDEFINITION_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEBOMDEFINITION_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_COMMERCEBOMFOLDERID_COMMERCEBOMFOLDERID_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEBOMDEFINITION_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			if (getDB().isSupportsInlineDistinct()) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator, true);
			}
			else {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_TABLE, orderByComparator, true);
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(CommerceBOMDefinitionModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(CommerceBOMDefinitionModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommerceBOMDefinition.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, CommerceBOMDefinitionImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, CommerceBOMDefinitionImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(commerceBOMFolderId);

			return (List<CommerceBOMDefinition>)QueryUtil.list(
				sqlQuery, getDialect(), start, end);
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns the commerce bom definitions before and after the current commerce bom definition in the ordered set of commerce bom definitions that the user has permission to view where commerceBOMFolderId = &#63;.
	 *
	 * @param commerceBOMDefinitionId the primary key of the current commerce bom definition
	 * @param commerceBOMFolderId the commerce bom folder ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce bom definition
	 * @throws NoSuchBOMDefinitionException if a commerce bom definition with the primary key could not be found
	 */
	@Override
	public CommerceBOMDefinition[] filterFindByCommerceBOMFolderId_PrevAndNext(
			long commerceBOMDefinitionId, long commerceBOMFolderId,
			OrderByComparator<CommerceBOMDefinition> orderByComparator)
		throws NoSuchBOMDefinitionException {

		if (!InlineSQLHelperUtil.isEnabled()) {
			return findByCommerceBOMFolderId_PrevAndNext(
				commerceBOMDefinitionId, commerceBOMFolderId,
				orderByComparator);
		}

		CommerceBOMDefinition commerceBOMDefinition = findByPrimaryKey(
			commerceBOMDefinitionId);

		Session session = null;

		try {
			session = openSession();

			CommerceBOMDefinition[] array = new CommerceBOMDefinitionImpl[3];

			array[0] = filterGetByCommerceBOMFolderId_PrevAndNext(
				session, commerceBOMDefinition, commerceBOMFolderId,
				orderByComparator, true);

			array[1] = commerceBOMDefinition;

			array[2] = filterGetByCommerceBOMFolderId_PrevAndNext(
				session, commerceBOMDefinition, commerceBOMFolderId,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected CommerceBOMDefinition filterGetByCommerceBOMFolderId_PrevAndNext(
		Session session, CommerceBOMDefinition commerceBOMDefinition,
		long commerceBOMFolderId,
		OrderByComparator<CommerceBOMDefinition> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_COMMERCEBOMDEFINITION_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEBOMDEFINITION_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_COMMERCEBOMFOLDERID_COMMERCEBOMFOLDERID_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEBOMDEFINITION_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByConditionFields[i],
							true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByConditionFields[i],
							true));
				}

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByFields[i], true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByFields[i], true));
				}

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(CommerceBOMDefinitionModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(CommerceBOMDefinitionModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommerceBOMDefinition.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(2);

		if (getDB().isSupportsInlineDistinct()) {
			sqlQuery.addEntity(
				_FILTER_ENTITY_ALIAS, CommerceBOMDefinitionImpl.class);
		}
		else {
			sqlQuery.addEntity(
				_FILTER_ENTITY_TABLE, CommerceBOMDefinitionImpl.class);
		}

		QueryPos queryPos = QueryPos.getInstance(sqlQuery);

		queryPos.add(commerceBOMFolderId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commerceBOMDefinition)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommerceBOMDefinition> list = sqlQuery.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce bom definitions where commerceBOMFolderId = &#63; from the database.
	 *
	 * @param commerceBOMFolderId the commerce bom folder ID
	 */
	@Override
	public void removeByCommerceBOMFolderId(long commerceBOMFolderId) {
		for (CommerceBOMDefinition commerceBOMDefinition :
				findByCommerceBOMFolderId(
					commerceBOMFolderId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(commerceBOMDefinition);
		}
	}

	/**
	 * Returns the number of commerce bom definitions where commerceBOMFolderId = &#63;.
	 *
	 * @param commerceBOMFolderId the commerce bom folder ID
	 * @return the number of matching commerce bom definitions
	 */
	@Override
	public int countByCommerceBOMFolderId(long commerceBOMFolderId) {
		FinderPath finderPath = _finderPathCountByCommerceBOMFolderId;

		Object[] finderArgs = new Object[] {commerceBOMFolderId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_COMMERCEBOMDEFINITION_WHERE);

			sb.append(_FINDER_COLUMN_COMMERCEBOMFOLDERID_COMMERCEBOMFOLDERID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commerceBOMFolderId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of commerce bom definitions that the user has permission to view where commerceBOMFolderId = &#63;.
	 *
	 * @param commerceBOMFolderId the commerce bom folder ID
	 * @return the number of matching commerce bom definitions that the user has permission to view
	 */
	@Override
	public int filterCountByCommerceBOMFolderId(long commerceBOMFolderId) {
		if (!InlineSQLHelperUtil.isEnabled()) {
			return countByCommerceBOMFolderId(commerceBOMFolderId);
		}

		StringBundler sb = new StringBundler(2);

		sb.append(_FILTER_SQL_COUNT_COMMERCEBOMDEFINITION_WHERE);

		sb.append(_FINDER_COLUMN_COMMERCEBOMFOLDERID_COMMERCEBOMFOLDERID_2);

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommerceBOMDefinition.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(commerceBOMFolderId);

			Long count = (Long)sqlQuery.uniqueResult();

			return count.intValue();
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	private static final String
		_FINDER_COLUMN_COMMERCEBOMFOLDERID_COMMERCEBOMFOLDERID_2 =
			"commerceBOMDefinition.commerceBOMFolderId = ?";

	public CommerceBOMDefinitionPersistenceImpl() {
		setModelClass(CommerceBOMDefinition.class);

		setModelImplClass(CommerceBOMDefinitionImpl.class);
		setModelPKClass(long.class);

		setTable(CommerceBOMDefinitionTable.INSTANCE);
	}

	/**
	 * Caches the commerce bom definition in the entity cache if it is enabled.
	 *
	 * @param commerceBOMDefinition the commerce bom definition
	 */
	@Override
	public void cacheResult(CommerceBOMDefinition commerceBOMDefinition) {
		entityCache.putResult(
			CommerceBOMDefinitionImpl.class,
			commerceBOMDefinition.getPrimaryKey(), commerceBOMDefinition);
	}

	/**
	 * Caches the commerce bom definitions in the entity cache if it is enabled.
	 *
	 * @param commerceBOMDefinitions the commerce bom definitions
	 */
	@Override
	public void cacheResult(
		List<CommerceBOMDefinition> commerceBOMDefinitions) {

		for (CommerceBOMDefinition commerceBOMDefinition :
				commerceBOMDefinitions) {

			if (entityCache.getResult(
					CommerceBOMDefinitionImpl.class,
					commerceBOMDefinition.getPrimaryKey()) == null) {

				cacheResult(commerceBOMDefinition);
			}
		}
	}

	/**
	 * Clears the cache for all commerce bom definitions.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(CommerceBOMDefinitionImpl.class);

		finderCache.clearCache(CommerceBOMDefinitionImpl.class);
	}

	/**
	 * Clears the cache for the commerce bom definition.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(CommerceBOMDefinition commerceBOMDefinition) {
		entityCache.removeResult(
			CommerceBOMDefinitionImpl.class, commerceBOMDefinition);
	}

	@Override
	public void clearCache(List<CommerceBOMDefinition> commerceBOMDefinitions) {
		for (CommerceBOMDefinition commerceBOMDefinition :
				commerceBOMDefinitions) {

			entityCache.removeResult(
				CommerceBOMDefinitionImpl.class, commerceBOMDefinition);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(CommerceBOMDefinitionImpl.class);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				CommerceBOMDefinitionImpl.class, primaryKey);
		}
	}

	/**
	 * Creates a new commerce bom definition with the primary key. Does not add the commerce bom definition to the database.
	 *
	 * @param commerceBOMDefinitionId the primary key for the new commerce bom definition
	 * @return the new commerce bom definition
	 */
	@Override
	public CommerceBOMDefinition create(long commerceBOMDefinitionId) {
		CommerceBOMDefinition commerceBOMDefinition =
			new CommerceBOMDefinitionImpl();

		commerceBOMDefinition.setNew(true);
		commerceBOMDefinition.setPrimaryKey(commerceBOMDefinitionId);

		commerceBOMDefinition.setCompanyId(CompanyThreadLocal.getCompanyId());

		return commerceBOMDefinition;
	}

	/**
	 * Removes the commerce bom definition with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commerceBOMDefinitionId the primary key of the commerce bom definition
	 * @return the commerce bom definition that was removed
	 * @throws NoSuchBOMDefinitionException if a commerce bom definition with the primary key could not be found
	 */
	@Override
	public CommerceBOMDefinition remove(long commerceBOMDefinitionId)
		throws NoSuchBOMDefinitionException {

		return remove((Serializable)commerceBOMDefinitionId);
	}

	/**
	 * Removes the commerce bom definition with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the commerce bom definition
	 * @return the commerce bom definition that was removed
	 * @throws NoSuchBOMDefinitionException if a commerce bom definition with the primary key could not be found
	 */
	@Override
	public CommerceBOMDefinition remove(Serializable primaryKey)
		throws NoSuchBOMDefinitionException {

		Session session = null;

		try {
			session = openSession();

			CommerceBOMDefinition commerceBOMDefinition =
				(CommerceBOMDefinition)session.get(
					CommerceBOMDefinitionImpl.class, primaryKey);

			if (commerceBOMDefinition == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchBOMDefinitionException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(commerceBOMDefinition);
		}
		catch (NoSuchBOMDefinitionException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected CommerceBOMDefinition removeImpl(
		CommerceBOMDefinition commerceBOMDefinition) {

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(commerceBOMDefinition)) {
				commerceBOMDefinition = (CommerceBOMDefinition)session.get(
					CommerceBOMDefinitionImpl.class,
					commerceBOMDefinition.getPrimaryKeyObj());
			}

			if (commerceBOMDefinition != null) {
				session.delete(commerceBOMDefinition);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (commerceBOMDefinition != null) {
			clearCache(commerceBOMDefinition);
		}

		return commerceBOMDefinition;
	}

	@Override
	public CommerceBOMDefinition updateImpl(
		CommerceBOMDefinition commerceBOMDefinition) {

		boolean isNew = commerceBOMDefinition.isNew();

		if (!(commerceBOMDefinition instanceof
				CommerceBOMDefinitionModelImpl)) {

			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(commerceBOMDefinition.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					commerceBOMDefinition);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in commerceBOMDefinition proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom CommerceBOMDefinition implementation " +
					commerceBOMDefinition.getClass());
		}

		CommerceBOMDefinitionModelImpl commerceBOMDefinitionModelImpl =
			(CommerceBOMDefinitionModelImpl)commerceBOMDefinition;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (commerceBOMDefinition.getCreateDate() == null)) {
			if (serviceContext == null) {
				commerceBOMDefinition.setCreateDate(date);
			}
			else {
				commerceBOMDefinition.setCreateDate(
					serviceContext.getCreateDate(date));
			}
		}

		if (!commerceBOMDefinitionModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				commerceBOMDefinition.setModifiedDate(date);
			}
			else {
				commerceBOMDefinition.setModifiedDate(
					serviceContext.getModifiedDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(commerceBOMDefinition);
			}
			else {
				commerceBOMDefinition = (CommerceBOMDefinition)session.merge(
					commerceBOMDefinition);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(
			CommerceBOMDefinitionImpl.class, commerceBOMDefinitionModelImpl,
			false, true);

		if (isNew) {
			commerceBOMDefinition.setNew(false);
		}

		commerceBOMDefinition.resetOriginalValues();

		return commerceBOMDefinition;
	}

	/**
	 * Returns the commerce bom definition with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the commerce bom definition
	 * @return the commerce bom definition
	 * @throws NoSuchBOMDefinitionException if a commerce bom definition with the primary key could not be found
	 */
	@Override
	public CommerceBOMDefinition findByPrimaryKey(Serializable primaryKey)
		throws NoSuchBOMDefinitionException {

		CommerceBOMDefinition commerceBOMDefinition = fetchByPrimaryKey(
			primaryKey);

		if (commerceBOMDefinition == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchBOMDefinitionException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return commerceBOMDefinition;
	}

	/**
	 * Returns the commerce bom definition with the primary key or throws a <code>NoSuchBOMDefinitionException</code> if it could not be found.
	 *
	 * @param commerceBOMDefinitionId the primary key of the commerce bom definition
	 * @return the commerce bom definition
	 * @throws NoSuchBOMDefinitionException if a commerce bom definition with the primary key could not be found
	 */
	@Override
	public CommerceBOMDefinition findByPrimaryKey(long commerceBOMDefinitionId)
		throws NoSuchBOMDefinitionException {

		return findByPrimaryKey((Serializable)commerceBOMDefinitionId);
	}

	/**
	 * Returns the commerce bom definition with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param commerceBOMDefinitionId the primary key of the commerce bom definition
	 * @return the commerce bom definition, or <code>null</code> if a commerce bom definition with the primary key could not be found
	 */
	@Override
	public CommerceBOMDefinition fetchByPrimaryKey(
		long commerceBOMDefinitionId) {

		return fetchByPrimaryKey((Serializable)commerceBOMDefinitionId);
	}

	/**
	 * Returns all the commerce bom definitions.
	 *
	 * @return the commerce bom definitions
	 */
	@Override
	public List<CommerceBOMDefinition> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce bom definitions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceBOMDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce bom definitions
	 * @param end the upper bound of the range of commerce bom definitions (not inclusive)
	 * @return the range of commerce bom definitions
	 */
	@Override
	public List<CommerceBOMDefinition> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce bom definitions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceBOMDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce bom definitions
	 * @param end the upper bound of the range of commerce bom definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of commerce bom definitions
	 */
	@Override
	public List<CommerceBOMDefinition> findAll(
		int start, int end,
		OrderByComparator<CommerceBOMDefinition> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce bom definitions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceBOMDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce bom definitions
	 * @param end the upper bound of the range of commerce bom definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of commerce bom definitions
	 */
	@Override
	public List<CommerceBOMDefinition> findAll(
		int start, int end,
		OrderByComparator<CommerceBOMDefinition> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<CommerceBOMDefinition> list = null;

		if (useFinderCache) {
			list = (List<CommerceBOMDefinition>)finderCache.getResult(
				finderPath, finderArgs);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_COMMERCEBOMDEFINITION);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_COMMERCEBOMDEFINITION;

				sql = sql.concat(CommerceBOMDefinitionModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<CommerceBOMDefinition>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the commerce bom definitions from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (CommerceBOMDefinition commerceBOMDefinition : findAll()) {
			remove(commerceBOMDefinition);
		}
	}

	/**
	 * Returns the number of commerce bom definitions.
	 *
	 * @return the number of commerce bom definitions
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(
					_SQL_COUNT_COMMERCEBOMDEFINITION);

				count = (Long)query.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "commerceBOMDefinitionId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_COMMERCEBOMDEFINITION;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return CommerceBOMDefinitionModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the commerce bom definition persistence.
	 */
	public void afterPropertiesSet() {
		Bundle bundle = FrameworkUtil.getBundle(
			CommerceBOMDefinitionPersistenceImpl.class);

		_bundleContext = bundle.getBundleContext();

		_argumentsResolverServiceRegistration = _bundleContext.registerService(
			ArgumentsResolver.class,
			new CommerceBOMDefinitionModelArgumentsResolver(),
			new HashMapDictionary<>());

		_finderPathWithPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		_finderPathWithPaginationFindByCommerceBOMFolderId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCommerceBOMFolderId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"commerceBOMFolderId"}, true);

		_finderPathWithoutPaginationFindByCommerceBOMFolderId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByCommerceBOMFolderId", new String[] {Long.class.getName()},
			new String[] {"commerceBOMFolderId"}, true);

		_finderPathCountByCommerceBOMFolderId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByCommerceBOMFolderId", new String[] {Long.class.getName()},
			new String[] {"commerceBOMFolderId"}, false);
	}

	public void destroy() {
		entityCache.removeCache(CommerceBOMDefinitionImpl.class.getName());

		_argumentsResolverServiceRegistration.unregister();
	}

	private BundleContext _bundleContext;

	@ServiceReference(type = EntityCache.class)
	protected EntityCache entityCache;

	@ServiceReference(type = FinderCache.class)
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_COMMERCEBOMDEFINITION =
		"SELECT commerceBOMDefinition FROM CommerceBOMDefinition commerceBOMDefinition";

	private static final String _SQL_SELECT_COMMERCEBOMDEFINITION_WHERE =
		"SELECT commerceBOMDefinition FROM CommerceBOMDefinition commerceBOMDefinition WHERE ";

	private static final String _SQL_COUNT_COMMERCEBOMDEFINITION =
		"SELECT COUNT(commerceBOMDefinition) FROM CommerceBOMDefinition commerceBOMDefinition";

	private static final String _SQL_COUNT_COMMERCEBOMDEFINITION_WHERE =
		"SELECT COUNT(commerceBOMDefinition) FROM CommerceBOMDefinition commerceBOMDefinition WHERE ";

	private static final String _FILTER_ENTITY_TABLE_FILTER_PK_COLUMN =
		"commerceBOMDefinition.commerceBOMDefinitionId";

	private static final String _FILTER_SQL_SELECT_COMMERCEBOMDEFINITION_WHERE =
		"SELECT DISTINCT {commerceBOMDefinition.*} FROM CommerceBOMDefinition commerceBOMDefinition WHERE ";

	private static final String
		_FILTER_SQL_SELECT_COMMERCEBOMDEFINITION_NO_INLINE_DISTINCT_WHERE_1 =
			"SELECT {CommerceBOMDefinition.*} FROM (SELECT DISTINCT commerceBOMDefinition.commerceBOMDefinitionId FROM CommerceBOMDefinition commerceBOMDefinition WHERE ";

	private static final String
		_FILTER_SQL_SELECT_COMMERCEBOMDEFINITION_NO_INLINE_DISTINCT_WHERE_2 =
			") TEMP_TABLE INNER JOIN CommerceBOMDefinition ON TEMP_TABLE.commerceBOMDefinitionId = CommerceBOMDefinition.commerceBOMDefinitionId";

	private static final String _FILTER_SQL_COUNT_COMMERCEBOMDEFINITION_WHERE =
		"SELECT COUNT(DISTINCT commerceBOMDefinition.commerceBOMDefinitionId) AS COUNT_VALUE FROM CommerceBOMDefinition commerceBOMDefinition WHERE ";

	private static final String _FILTER_ENTITY_ALIAS = "commerceBOMDefinition";

	private static final String _FILTER_ENTITY_TABLE = "CommerceBOMDefinition";

	private static final String _ORDER_BY_ENTITY_ALIAS =
		"commerceBOMDefinition.";

	private static final String _ORDER_BY_ENTITY_TABLE =
		"CommerceBOMDefinition.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No CommerceBOMDefinition exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No CommerceBOMDefinition exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceBOMDefinitionPersistenceImpl.class);

	@Override
	protected FinderCache getFinderCache() {
		return finderCache;
	}

	private ServiceRegistration<ArgumentsResolver>
		_argumentsResolverServiceRegistration;

	private static class CommerceBOMDefinitionModelArgumentsResolver
		implements ArgumentsResolver {

		@Override
		public Object[] getArguments(
			FinderPath finderPath, BaseModel<?> baseModel, boolean checkColumn,
			boolean original) {

			String[] columnNames = finderPath.getColumnNames();

			if ((columnNames == null) || (columnNames.length == 0)) {
				if (baseModel.isNew()) {
					return FINDER_ARGS_EMPTY;
				}

				return null;
			}

			CommerceBOMDefinitionModelImpl commerceBOMDefinitionModelImpl =
				(CommerceBOMDefinitionModelImpl)baseModel;

			long columnBitmask =
				commerceBOMDefinitionModelImpl.getColumnBitmask();

			if (!checkColumn || (columnBitmask == 0)) {
				return _getValue(
					commerceBOMDefinitionModelImpl, columnNames, original);
			}

			Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
				finderPath);

			if (finderPathColumnBitmask == null) {
				finderPathColumnBitmask = 0L;

				for (String columnName : columnNames) {
					finderPathColumnBitmask |=
						commerceBOMDefinitionModelImpl.getColumnBitmask(
							columnName);
				}

				if (finderPath.isBaseModelResult() &&
					(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION ==
						finderPath.getCacheName())) {

					finderPathColumnBitmask |= _ORDER_BY_COLUMNS_BITMASK;
				}

				_finderPathColumnBitmasksCache.put(
					finderPath, finderPathColumnBitmask);
			}

			if ((columnBitmask & finderPathColumnBitmask) != 0) {
				return _getValue(
					commerceBOMDefinitionModelImpl, columnNames, original);
			}

			return null;
		}

		@Override
		public String getClassName() {
			return CommerceBOMDefinitionImpl.class.getName();
		}

		@Override
		public String getTableName() {
			return CommerceBOMDefinitionTable.INSTANCE.getTableName();
		}

		private static Object[] _getValue(
			CommerceBOMDefinitionModelImpl commerceBOMDefinitionModelImpl,
			String[] columnNames, boolean original) {

			Object[] arguments = new Object[columnNames.length];

			for (int i = 0; i < arguments.length; i++) {
				String columnName = columnNames[i];

				if (original) {
					arguments[i] =
						commerceBOMDefinitionModelImpl.getColumnOriginalValue(
							columnName);
				}
				else {
					arguments[i] =
						commerceBOMDefinitionModelImpl.getColumnValue(
							columnName);
				}
			}

			return arguments;
		}

		private static final Map<FinderPath, Long>
			_finderPathColumnBitmasksCache = new ConcurrentHashMap<>();

		private static final long _ORDER_BY_COLUMNS_BITMASK;

		static {
			long orderByColumnsBitmask = 0;

			orderByColumnsBitmask |=
				CommerceBOMDefinitionModelImpl.getColumnBitmask("name");

			_ORDER_BY_COLUMNS_BITMASK = orderByColumnsBitmask;
		}

	}

}