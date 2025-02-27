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

package com.liferay.batch.planner.service.persistence.impl;

import com.liferay.batch.planner.exception.NoSuchPlanException;
import com.liferay.batch.planner.model.BatchPlannerPlan;
import com.liferay.batch.planner.model.BatchPlannerPlanTable;
import com.liferay.batch.planner.model.impl.BatchPlannerPlanImpl;
import com.liferay.batch.planner.model.impl.BatchPlannerPlanModelImpl;
import com.liferay.batch.planner.service.persistence.BatchPlannerPlanPersistence;
import com.liferay.batch.planner.service.persistence.impl.constants.BatchPlannerPersistenceConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.ArgumentsResolver;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.permission.InlineSQLHelperUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the batch planner plan service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Igor Beslic
 * @generated
 */
@Component(service = {BatchPlannerPlanPersistence.class, BasePersistence.class})
public class BatchPlannerPlanPersistenceImpl
	extends BasePersistenceImpl<BatchPlannerPlan>
	implements BatchPlannerPlanPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>BatchPlannerPlanUtil</code> to access the batch planner plan persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		BatchPlannerPlanImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByCompanyId;
	private FinderPath _finderPathWithoutPaginationFindByCompanyId;
	private FinderPath _finderPathCountByCompanyId;

	/**
	 * Returns all the batch planner plans where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching batch planner plans
	 */
	@Override
	public List<BatchPlannerPlan> findByCompanyId(long companyId) {
		return findByCompanyId(
			companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the batch planner plans where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>BatchPlannerPlanModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of batch planner plans
	 * @param end the upper bound of the range of batch planner plans (not inclusive)
	 * @return the range of matching batch planner plans
	 */
	@Override
	public List<BatchPlannerPlan> findByCompanyId(
		long companyId, int start, int end) {

		return findByCompanyId(companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the batch planner plans where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>BatchPlannerPlanModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of batch planner plans
	 * @param end the upper bound of the range of batch planner plans (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching batch planner plans
	 */
	@Override
	public List<BatchPlannerPlan> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<BatchPlannerPlan> orderByComparator) {

		return findByCompanyId(companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the batch planner plans where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>BatchPlannerPlanModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of batch planner plans
	 * @param end the upper bound of the range of batch planner plans (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching batch planner plans
	 */
	@Override
	public List<BatchPlannerPlan> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<BatchPlannerPlan> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByCompanyId;
				finderArgs = new Object[] {companyId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCompanyId;
			finderArgs = new Object[] {
				companyId, start, end, orderByComparator
			};
		}

		List<BatchPlannerPlan> list = null;

		if (useFinderCache) {
			list = (List<BatchPlannerPlan>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (BatchPlannerPlan batchPlannerPlan : list) {
					if (companyId != batchPlannerPlan.getCompanyId()) {
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

			sb.append(_SQL_SELECT_BATCHPLANNERPLAN_WHERE);

			sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(BatchPlannerPlanModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				list = (List<BatchPlannerPlan>)QueryUtil.list(
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
	 * Returns the first batch planner plan in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching batch planner plan
	 * @throws NoSuchPlanException if a matching batch planner plan could not be found
	 */
	@Override
	public BatchPlannerPlan findByCompanyId_First(
			long companyId,
			OrderByComparator<BatchPlannerPlan> orderByComparator)
		throws NoSuchPlanException {

		BatchPlannerPlan batchPlannerPlan = fetchByCompanyId_First(
			companyId, orderByComparator);

		if (batchPlannerPlan != null) {
			return batchPlannerPlan;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchPlanException(sb.toString());
	}

	/**
	 * Returns the first batch planner plan in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching batch planner plan, or <code>null</code> if a matching batch planner plan could not be found
	 */
	@Override
	public BatchPlannerPlan fetchByCompanyId_First(
		long companyId, OrderByComparator<BatchPlannerPlan> orderByComparator) {

		List<BatchPlannerPlan> list = findByCompanyId(
			companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last batch planner plan in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching batch planner plan
	 * @throws NoSuchPlanException if a matching batch planner plan could not be found
	 */
	@Override
	public BatchPlannerPlan findByCompanyId_Last(
			long companyId,
			OrderByComparator<BatchPlannerPlan> orderByComparator)
		throws NoSuchPlanException {

		BatchPlannerPlan batchPlannerPlan = fetchByCompanyId_Last(
			companyId, orderByComparator);

		if (batchPlannerPlan != null) {
			return batchPlannerPlan;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchPlanException(sb.toString());
	}

	/**
	 * Returns the last batch planner plan in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching batch planner plan, or <code>null</code> if a matching batch planner plan could not be found
	 */
	@Override
	public BatchPlannerPlan fetchByCompanyId_Last(
		long companyId, OrderByComparator<BatchPlannerPlan> orderByComparator) {

		int count = countByCompanyId(companyId);

		if (count == 0) {
			return null;
		}

		List<BatchPlannerPlan> list = findByCompanyId(
			companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the batch planner plans before and after the current batch planner plan in the ordered set where companyId = &#63;.
	 *
	 * @param batchPlannerPlanId the primary key of the current batch planner plan
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next batch planner plan
	 * @throws NoSuchPlanException if a batch planner plan with the primary key could not be found
	 */
	@Override
	public BatchPlannerPlan[] findByCompanyId_PrevAndNext(
			long batchPlannerPlanId, long companyId,
			OrderByComparator<BatchPlannerPlan> orderByComparator)
		throws NoSuchPlanException {

		BatchPlannerPlan batchPlannerPlan = findByPrimaryKey(
			batchPlannerPlanId);

		Session session = null;

		try {
			session = openSession();

			BatchPlannerPlan[] array = new BatchPlannerPlanImpl[3];

			array[0] = getByCompanyId_PrevAndNext(
				session, batchPlannerPlan, companyId, orderByComparator, true);

			array[1] = batchPlannerPlan;

			array[2] = getByCompanyId_PrevAndNext(
				session, batchPlannerPlan, companyId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected BatchPlannerPlan getByCompanyId_PrevAndNext(
		Session session, BatchPlannerPlan batchPlannerPlan, long companyId,
		OrderByComparator<BatchPlannerPlan> orderByComparator,
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

		sb.append(_SQL_SELECT_BATCHPLANNERPLAN_WHERE);

		sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

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
			sb.append(BatchPlannerPlanModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						batchPlannerPlan)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<BatchPlannerPlan> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the batch planner plans that the user has permission to view where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching batch planner plans that the user has permission to view
	 */
	@Override
	public List<BatchPlannerPlan> filterFindByCompanyId(long companyId) {
		return filterFindByCompanyId(
			companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the batch planner plans that the user has permission to view where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>BatchPlannerPlanModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of batch planner plans
	 * @param end the upper bound of the range of batch planner plans (not inclusive)
	 * @return the range of matching batch planner plans that the user has permission to view
	 */
	@Override
	public List<BatchPlannerPlan> filterFindByCompanyId(
		long companyId, int start, int end) {

		return filterFindByCompanyId(companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the batch planner plans that the user has permissions to view where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>BatchPlannerPlanModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of batch planner plans
	 * @param end the upper bound of the range of batch planner plans (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching batch planner plans that the user has permission to view
	 */
	@Override
	public List<BatchPlannerPlan> filterFindByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<BatchPlannerPlan> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled(companyId, 0)) {
			return findByCompanyId(companyId, start, end, orderByComparator);
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
			sb.append(_FILTER_SQL_SELECT_BATCHPLANNERPLAN_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_BATCHPLANNERPLAN_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_BATCHPLANNERPLAN_NO_INLINE_DISTINCT_WHERE_2);
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
				sb.append(BatchPlannerPlanModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(BatchPlannerPlanModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), BatchPlannerPlan.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, BatchPlannerPlanImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, BatchPlannerPlanImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(companyId);

			return (List<BatchPlannerPlan>)QueryUtil.list(
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
	 * Returns the batch planner plans before and after the current batch planner plan in the ordered set of batch planner plans that the user has permission to view where companyId = &#63;.
	 *
	 * @param batchPlannerPlanId the primary key of the current batch planner plan
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next batch planner plan
	 * @throws NoSuchPlanException if a batch planner plan with the primary key could not be found
	 */
	@Override
	public BatchPlannerPlan[] filterFindByCompanyId_PrevAndNext(
			long batchPlannerPlanId, long companyId,
			OrderByComparator<BatchPlannerPlan> orderByComparator)
		throws NoSuchPlanException {

		if (!InlineSQLHelperUtil.isEnabled(companyId, 0)) {
			return findByCompanyId_PrevAndNext(
				batchPlannerPlanId, companyId, orderByComparator);
		}

		BatchPlannerPlan batchPlannerPlan = findByPrimaryKey(
			batchPlannerPlanId);

		Session session = null;

		try {
			session = openSession();

			BatchPlannerPlan[] array = new BatchPlannerPlanImpl[3];

			array[0] = filterGetByCompanyId_PrevAndNext(
				session, batchPlannerPlan, companyId, orderByComparator, true);

			array[1] = batchPlannerPlan;

			array[2] = filterGetByCompanyId_PrevAndNext(
				session, batchPlannerPlan, companyId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected BatchPlannerPlan filterGetByCompanyId_PrevAndNext(
		Session session, BatchPlannerPlan batchPlannerPlan, long companyId,
		OrderByComparator<BatchPlannerPlan> orderByComparator,
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
			sb.append(_FILTER_SQL_SELECT_BATCHPLANNERPLAN_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_BATCHPLANNERPLAN_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_BATCHPLANNERPLAN_NO_INLINE_DISTINCT_WHERE_2);
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
				sb.append(BatchPlannerPlanModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(BatchPlannerPlanModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), BatchPlannerPlan.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(2);

		if (getDB().isSupportsInlineDistinct()) {
			sqlQuery.addEntity(
				_FILTER_ENTITY_ALIAS, BatchPlannerPlanImpl.class);
		}
		else {
			sqlQuery.addEntity(
				_FILTER_ENTITY_TABLE, BatchPlannerPlanImpl.class);
		}

		QueryPos queryPos = QueryPos.getInstance(sqlQuery);

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						batchPlannerPlan)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<BatchPlannerPlan> list = sqlQuery.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the batch planner plans where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	@Override
	public void removeByCompanyId(long companyId) {
		for (BatchPlannerPlan batchPlannerPlan :
				findByCompanyId(
					companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(batchPlannerPlan);
		}
	}

	/**
	 * Returns the number of batch planner plans where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching batch planner plans
	 */
	@Override
	public int countByCompanyId(long companyId) {
		FinderPath finderPath = _finderPathCountByCompanyId;

		Object[] finderArgs = new Object[] {companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_BATCHPLANNERPLAN_WHERE);

			sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

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
	 * Returns the number of batch planner plans that the user has permission to view where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching batch planner plans that the user has permission to view
	 */
	@Override
	public int filterCountByCompanyId(long companyId) {
		if (!InlineSQLHelperUtil.isEnabled(companyId, 0)) {
			return countByCompanyId(companyId);
		}

		StringBundler sb = new StringBundler(2);

		sb.append(_FILTER_SQL_COUNT_BATCHPLANNERPLAN_WHERE);

		sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), BatchPlannerPlan.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(companyId);

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

	private static final String _FINDER_COLUMN_COMPANYID_COMPANYID_2 =
		"batchPlannerPlan.companyId = ?";

	private FinderPath _finderPathWithPaginationFindByC_U;
	private FinderPath _finderPathWithoutPaginationFindByC_U;
	private FinderPath _finderPathCountByC_U;

	/**
	 * Returns all the batch planner plans where companyId = &#63; and userId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @return the matching batch planner plans
	 */
	@Override
	public List<BatchPlannerPlan> findByC_U(long companyId, long userId) {
		return findByC_U(
			companyId, userId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the batch planner plans where companyId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>BatchPlannerPlanModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of batch planner plans
	 * @param end the upper bound of the range of batch planner plans (not inclusive)
	 * @return the range of matching batch planner plans
	 */
	@Override
	public List<BatchPlannerPlan> findByC_U(
		long companyId, long userId, int start, int end) {

		return findByC_U(companyId, userId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the batch planner plans where companyId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>BatchPlannerPlanModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of batch planner plans
	 * @param end the upper bound of the range of batch planner plans (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching batch planner plans
	 */
	@Override
	public List<BatchPlannerPlan> findByC_U(
		long companyId, long userId, int start, int end,
		OrderByComparator<BatchPlannerPlan> orderByComparator) {

		return findByC_U(
			companyId, userId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the batch planner plans where companyId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>BatchPlannerPlanModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of batch planner plans
	 * @param end the upper bound of the range of batch planner plans (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching batch planner plans
	 */
	@Override
	public List<BatchPlannerPlan> findByC_U(
		long companyId, long userId, int start, int end,
		OrderByComparator<BatchPlannerPlan> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByC_U;
				finderArgs = new Object[] {companyId, userId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByC_U;
			finderArgs = new Object[] {
				companyId, userId, start, end, orderByComparator
			};
		}

		List<BatchPlannerPlan> list = null;

		if (useFinderCache) {
			list = (List<BatchPlannerPlan>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (BatchPlannerPlan batchPlannerPlan : list) {
					if ((companyId != batchPlannerPlan.getCompanyId()) ||
						(userId != batchPlannerPlan.getUserId())) {

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
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_BATCHPLANNERPLAN_WHERE);

			sb.append(_FINDER_COLUMN_C_U_COMPANYID_2);

			sb.append(_FINDER_COLUMN_C_U_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(BatchPlannerPlanModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				queryPos.add(userId);

				list = (List<BatchPlannerPlan>)QueryUtil.list(
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
	 * Returns the first batch planner plan in the ordered set where companyId = &#63; and userId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching batch planner plan
	 * @throws NoSuchPlanException if a matching batch planner plan could not be found
	 */
	@Override
	public BatchPlannerPlan findByC_U_First(
			long companyId, long userId,
			OrderByComparator<BatchPlannerPlan> orderByComparator)
		throws NoSuchPlanException {

		BatchPlannerPlan batchPlannerPlan = fetchByC_U_First(
			companyId, userId, orderByComparator);

		if (batchPlannerPlan != null) {
			return batchPlannerPlan;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append(", userId=");
		sb.append(userId);

		sb.append("}");

		throw new NoSuchPlanException(sb.toString());
	}

	/**
	 * Returns the first batch planner plan in the ordered set where companyId = &#63; and userId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching batch planner plan, or <code>null</code> if a matching batch planner plan could not be found
	 */
	@Override
	public BatchPlannerPlan fetchByC_U_First(
		long companyId, long userId,
		OrderByComparator<BatchPlannerPlan> orderByComparator) {

		List<BatchPlannerPlan> list = findByC_U(
			companyId, userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last batch planner plan in the ordered set where companyId = &#63; and userId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching batch planner plan
	 * @throws NoSuchPlanException if a matching batch planner plan could not be found
	 */
	@Override
	public BatchPlannerPlan findByC_U_Last(
			long companyId, long userId,
			OrderByComparator<BatchPlannerPlan> orderByComparator)
		throws NoSuchPlanException {

		BatchPlannerPlan batchPlannerPlan = fetchByC_U_Last(
			companyId, userId, orderByComparator);

		if (batchPlannerPlan != null) {
			return batchPlannerPlan;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append(", userId=");
		sb.append(userId);

		sb.append("}");

		throw new NoSuchPlanException(sb.toString());
	}

	/**
	 * Returns the last batch planner plan in the ordered set where companyId = &#63; and userId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching batch planner plan, or <code>null</code> if a matching batch planner plan could not be found
	 */
	@Override
	public BatchPlannerPlan fetchByC_U_Last(
		long companyId, long userId,
		OrderByComparator<BatchPlannerPlan> orderByComparator) {

		int count = countByC_U(companyId, userId);

		if (count == 0) {
			return null;
		}

		List<BatchPlannerPlan> list = findByC_U(
			companyId, userId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the batch planner plans before and after the current batch planner plan in the ordered set where companyId = &#63; and userId = &#63;.
	 *
	 * @param batchPlannerPlanId the primary key of the current batch planner plan
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next batch planner plan
	 * @throws NoSuchPlanException if a batch planner plan with the primary key could not be found
	 */
	@Override
	public BatchPlannerPlan[] findByC_U_PrevAndNext(
			long batchPlannerPlanId, long companyId, long userId,
			OrderByComparator<BatchPlannerPlan> orderByComparator)
		throws NoSuchPlanException {

		BatchPlannerPlan batchPlannerPlan = findByPrimaryKey(
			batchPlannerPlanId);

		Session session = null;

		try {
			session = openSession();

			BatchPlannerPlan[] array = new BatchPlannerPlanImpl[3];

			array[0] = getByC_U_PrevAndNext(
				session, batchPlannerPlan, companyId, userId, orderByComparator,
				true);

			array[1] = batchPlannerPlan;

			array[2] = getByC_U_PrevAndNext(
				session, batchPlannerPlan, companyId, userId, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected BatchPlannerPlan getByC_U_PrevAndNext(
		Session session, BatchPlannerPlan batchPlannerPlan, long companyId,
		long userId, OrderByComparator<BatchPlannerPlan> orderByComparator,
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

		sb.append(_SQL_SELECT_BATCHPLANNERPLAN_WHERE);

		sb.append(_FINDER_COLUMN_C_U_COMPANYID_2);

		sb.append(_FINDER_COLUMN_C_U_USERID_2);

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
			sb.append(BatchPlannerPlanModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(companyId);

		queryPos.add(userId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						batchPlannerPlan)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<BatchPlannerPlan> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the batch planner plans that the user has permission to view where companyId = &#63; and userId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @return the matching batch planner plans that the user has permission to view
	 */
	@Override
	public List<BatchPlannerPlan> filterFindByC_U(long companyId, long userId) {
		return filterFindByC_U(
			companyId, userId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the batch planner plans that the user has permission to view where companyId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>BatchPlannerPlanModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of batch planner plans
	 * @param end the upper bound of the range of batch planner plans (not inclusive)
	 * @return the range of matching batch planner plans that the user has permission to view
	 */
	@Override
	public List<BatchPlannerPlan> filterFindByC_U(
		long companyId, long userId, int start, int end) {

		return filterFindByC_U(companyId, userId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the batch planner plans that the user has permissions to view where companyId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>BatchPlannerPlanModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of batch planner plans
	 * @param end the upper bound of the range of batch planner plans (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching batch planner plans that the user has permission to view
	 */
	@Override
	public List<BatchPlannerPlan> filterFindByC_U(
		long companyId, long userId, int start, int end,
		OrderByComparator<BatchPlannerPlan> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled(companyId, 0)) {
			return findByC_U(companyId, userId, start, end, orderByComparator);
		}

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByFields().length * 2));
		}
		else {
			sb = new StringBundler(5);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_BATCHPLANNERPLAN_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_BATCHPLANNERPLAN_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_C_U_COMPANYID_2);

		sb.append(_FINDER_COLUMN_C_U_USERID_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_BATCHPLANNERPLAN_NO_INLINE_DISTINCT_WHERE_2);
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
				sb.append(BatchPlannerPlanModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(BatchPlannerPlanModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), BatchPlannerPlan.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, BatchPlannerPlanImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, BatchPlannerPlanImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(companyId);

			queryPos.add(userId);

			return (List<BatchPlannerPlan>)QueryUtil.list(
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
	 * Returns the batch planner plans before and after the current batch planner plan in the ordered set of batch planner plans that the user has permission to view where companyId = &#63; and userId = &#63;.
	 *
	 * @param batchPlannerPlanId the primary key of the current batch planner plan
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next batch planner plan
	 * @throws NoSuchPlanException if a batch planner plan with the primary key could not be found
	 */
	@Override
	public BatchPlannerPlan[] filterFindByC_U_PrevAndNext(
			long batchPlannerPlanId, long companyId, long userId,
			OrderByComparator<BatchPlannerPlan> orderByComparator)
		throws NoSuchPlanException {

		if (!InlineSQLHelperUtil.isEnabled(companyId, 0)) {
			return findByC_U_PrevAndNext(
				batchPlannerPlanId, companyId, userId, orderByComparator);
		}

		BatchPlannerPlan batchPlannerPlan = findByPrimaryKey(
			batchPlannerPlanId);

		Session session = null;

		try {
			session = openSession();

			BatchPlannerPlan[] array = new BatchPlannerPlanImpl[3];

			array[0] = filterGetByC_U_PrevAndNext(
				session, batchPlannerPlan, companyId, userId, orderByComparator,
				true);

			array[1] = batchPlannerPlan;

			array[2] = filterGetByC_U_PrevAndNext(
				session, batchPlannerPlan, companyId, userId, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected BatchPlannerPlan filterGetByC_U_PrevAndNext(
		Session session, BatchPlannerPlan batchPlannerPlan, long companyId,
		long userId, OrderByComparator<BatchPlannerPlan> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				6 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(5);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_BATCHPLANNERPLAN_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_BATCHPLANNERPLAN_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_C_U_COMPANYID_2);

		sb.append(_FINDER_COLUMN_C_U_USERID_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_BATCHPLANNERPLAN_NO_INLINE_DISTINCT_WHERE_2);
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
				sb.append(BatchPlannerPlanModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(BatchPlannerPlanModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), BatchPlannerPlan.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(2);

		if (getDB().isSupportsInlineDistinct()) {
			sqlQuery.addEntity(
				_FILTER_ENTITY_ALIAS, BatchPlannerPlanImpl.class);
		}
		else {
			sqlQuery.addEntity(
				_FILTER_ENTITY_TABLE, BatchPlannerPlanImpl.class);
		}

		QueryPos queryPos = QueryPos.getInstance(sqlQuery);

		queryPos.add(companyId);

		queryPos.add(userId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						batchPlannerPlan)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<BatchPlannerPlan> list = sqlQuery.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the batch planner plans where companyId = &#63; and userId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 */
	@Override
	public void removeByC_U(long companyId, long userId) {
		for (BatchPlannerPlan batchPlannerPlan :
				findByC_U(
					companyId, userId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(batchPlannerPlan);
		}
	}

	/**
	 * Returns the number of batch planner plans where companyId = &#63; and userId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @return the number of matching batch planner plans
	 */
	@Override
	public int countByC_U(long companyId, long userId) {
		FinderPath finderPath = _finderPathCountByC_U;

		Object[] finderArgs = new Object[] {companyId, userId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_BATCHPLANNERPLAN_WHERE);

			sb.append(_FINDER_COLUMN_C_U_COMPANYID_2);

			sb.append(_FINDER_COLUMN_C_U_USERID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				queryPos.add(userId);

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
	 * Returns the number of batch planner plans that the user has permission to view where companyId = &#63; and userId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param userId the user ID
	 * @return the number of matching batch planner plans that the user has permission to view
	 */
	@Override
	public int filterCountByC_U(long companyId, long userId) {
		if (!InlineSQLHelperUtil.isEnabled(companyId, 0)) {
			return countByC_U(companyId, userId);
		}

		StringBundler sb = new StringBundler(3);

		sb.append(_FILTER_SQL_COUNT_BATCHPLANNERPLAN_WHERE);

		sb.append(_FINDER_COLUMN_C_U_COMPANYID_2);

		sb.append(_FINDER_COLUMN_C_U_USERID_2);

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), BatchPlannerPlan.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(companyId);

			queryPos.add(userId);

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

	private static final String _FINDER_COLUMN_C_U_COMPANYID_2 =
		"batchPlannerPlan.companyId = ? AND ";

	private static final String _FINDER_COLUMN_C_U_USERID_2 =
		"batchPlannerPlan.userId = ?";

	private FinderPath _finderPathFetchByC_N;
	private FinderPath _finderPathCountByC_N;

	/**
	 * Returns the batch planner plan where companyId = &#63; and name = &#63; or throws a <code>NoSuchPlanException</code> if it could not be found.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @return the matching batch planner plan
	 * @throws NoSuchPlanException if a matching batch planner plan could not be found
	 */
	@Override
	public BatchPlannerPlan findByC_N(long companyId, String name)
		throws NoSuchPlanException {

		BatchPlannerPlan batchPlannerPlan = fetchByC_N(companyId, name);

		if (batchPlannerPlan == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("companyId=");
			sb.append(companyId);

			sb.append(", name=");
			sb.append(name);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchPlanException(sb.toString());
		}

		return batchPlannerPlan;
	}

	/**
	 * Returns the batch planner plan where companyId = &#63; and name = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @return the matching batch planner plan, or <code>null</code> if a matching batch planner plan could not be found
	 */
	@Override
	public BatchPlannerPlan fetchByC_N(long companyId, String name) {
		return fetchByC_N(companyId, name, true);
	}

	/**
	 * Returns the batch planner plan where companyId = &#63; and name = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching batch planner plan, or <code>null</code> if a matching batch planner plan could not be found
	 */
	@Override
	public BatchPlannerPlan fetchByC_N(
		long companyId, String name, boolean useFinderCache) {

		name = Objects.toString(name, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {companyId, name};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(_finderPathFetchByC_N, finderArgs);
		}

		if (result instanceof BatchPlannerPlan) {
			BatchPlannerPlan batchPlannerPlan = (BatchPlannerPlan)result;

			if ((companyId != batchPlannerPlan.getCompanyId()) ||
				!Objects.equals(name, batchPlannerPlan.getName())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_BATCHPLANNERPLAN_WHERE);

			sb.append(_FINDER_COLUMN_C_N_COMPANYID_2);

			boolean bindName = false;

			if (name.isEmpty()) {
				sb.append(_FINDER_COLUMN_C_N_NAME_3);
			}
			else {
				bindName = true;

				sb.append(_FINDER_COLUMN_C_N_NAME_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				if (bindName) {
					queryPos.add(name);
				}

				List<BatchPlannerPlan> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByC_N, finderArgs, list);
					}
				}
				else {
					BatchPlannerPlan batchPlannerPlan = list.get(0);

					result = batchPlannerPlan;

					cacheResult(batchPlannerPlan);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (BatchPlannerPlan)result;
		}
	}

	/**
	 * Removes the batch planner plan where companyId = &#63; and name = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @return the batch planner plan that was removed
	 */
	@Override
	public BatchPlannerPlan removeByC_N(long companyId, String name)
		throws NoSuchPlanException {

		BatchPlannerPlan batchPlannerPlan = findByC_N(companyId, name);

		return remove(batchPlannerPlan);
	}

	/**
	 * Returns the number of batch planner plans where companyId = &#63; and name = &#63;.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @return the number of matching batch planner plans
	 */
	@Override
	public int countByC_N(long companyId, String name) {
		name = Objects.toString(name, "");

		FinderPath finderPath = _finderPathCountByC_N;

		Object[] finderArgs = new Object[] {companyId, name};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_BATCHPLANNERPLAN_WHERE);

			sb.append(_FINDER_COLUMN_C_N_COMPANYID_2);

			boolean bindName = false;

			if (name.isEmpty()) {
				sb.append(_FINDER_COLUMN_C_N_NAME_3);
			}
			else {
				bindName = true;

				sb.append(_FINDER_COLUMN_C_N_NAME_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				if (bindName) {
					queryPos.add(name);
				}

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

	private static final String _FINDER_COLUMN_C_N_COMPANYID_2 =
		"batchPlannerPlan.companyId = ? AND ";

	private static final String _FINDER_COLUMN_C_N_NAME_2 =
		"batchPlannerPlan.name = ?";

	private static final String _FINDER_COLUMN_C_N_NAME_3 =
		"(batchPlannerPlan.name IS NULL OR batchPlannerPlan.name = '')";

	public BatchPlannerPlanPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("active", "active_");

		setDBColumnNames(dbColumnNames);

		setModelClass(BatchPlannerPlan.class);

		setModelImplClass(BatchPlannerPlanImpl.class);
		setModelPKClass(long.class);

		setTable(BatchPlannerPlanTable.INSTANCE);
	}

	/**
	 * Caches the batch planner plan in the entity cache if it is enabled.
	 *
	 * @param batchPlannerPlan the batch planner plan
	 */
	@Override
	public void cacheResult(BatchPlannerPlan batchPlannerPlan) {
		entityCache.putResult(
			BatchPlannerPlanImpl.class, batchPlannerPlan.getPrimaryKey(),
			batchPlannerPlan);

		finderCache.putResult(
			_finderPathFetchByC_N,
			new Object[] {
				batchPlannerPlan.getCompanyId(), batchPlannerPlan.getName()
			},
			batchPlannerPlan);
	}

	/**
	 * Caches the batch planner plans in the entity cache if it is enabled.
	 *
	 * @param batchPlannerPlans the batch planner plans
	 */
	@Override
	public void cacheResult(List<BatchPlannerPlan> batchPlannerPlans) {
		for (BatchPlannerPlan batchPlannerPlan : batchPlannerPlans) {
			if (entityCache.getResult(
					BatchPlannerPlanImpl.class,
					batchPlannerPlan.getPrimaryKey()) == null) {

				cacheResult(batchPlannerPlan);
			}
		}
	}

	/**
	 * Clears the cache for all batch planner plans.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(BatchPlannerPlanImpl.class);

		finderCache.clearCache(BatchPlannerPlanImpl.class);
	}

	/**
	 * Clears the cache for the batch planner plan.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(BatchPlannerPlan batchPlannerPlan) {
		entityCache.removeResult(BatchPlannerPlanImpl.class, batchPlannerPlan);
	}

	@Override
	public void clearCache(List<BatchPlannerPlan> batchPlannerPlans) {
		for (BatchPlannerPlan batchPlannerPlan : batchPlannerPlans) {
			entityCache.removeResult(
				BatchPlannerPlanImpl.class, batchPlannerPlan);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(BatchPlannerPlanImpl.class);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(BatchPlannerPlanImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		BatchPlannerPlanModelImpl batchPlannerPlanModelImpl) {

		Object[] args = new Object[] {
			batchPlannerPlanModelImpl.getCompanyId(),
			batchPlannerPlanModelImpl.getName()
		};

		finderCache.putResult(_finderPathCountByC_N, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByC_N, args, batchPlannerPlanModelImpl);
	}

	/**
	 * Creates a new batch planner plan with the primary key. Does not add the batch planner plan to the database.
	 *
	 * @param batchPlannerPlanId the primary key for the new batch planner plan
	 * @return the new batch planner plan
	 */
	@Override
	public BatchPlannerPlan create(long batchPlannerPlanId) {
		BatchPlannerPlan batchPlannerPlan = new BatchPlannerPlanImpl();

		batchPlannerPlan.setNew(true);
		batchPlannerPlan.setPrimaryKey(batchPlannerPlanId);

		batchPlannerPlan.setCompanyId(CompanyThreadLocal.getCompanyId());

		return batchPlannerPlan;
	}

	/**
	 * Removes the batch planner plan with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param batchPlannerPlanId the primary key of the batch planner plan
	 * @return the batch planner plan that was removed
	 * @throws NoSuchPlanException if a batch planner plan with the primary key could not be found
	 */
	@Override
	public BatchPlannerPlan remove(long batchPlannerPlanId)
		throws NoSuchPlanException {

		return remove((Serializable)batchPlannerPlanId);
	}

	/**
	 * Removes the batch planner plan with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the batch planner plan
	 * @return the batch planner plan that was removed
	 * @throws NoSuchPlanException if a batch planner plan with the primary key could not be found
	 */
	@Override
	public BatchPlannerPlan remove(Serializable primaryKey)
		throws NoSuchPlanException {

		Session session = null;

		try {
			session = openSession();

			BatchPlannerPlan batchPlannerPlan = (BatchPlannerPlan)session.get(
				BatchPlannerPlanImpl.class, primaryKey);

			if (batchPlannerPlan == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchPlanException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(batchPlannerPlan);
		}
		catch (NoSuchPlanException noSuchEntityException) {
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
	protected BatchPlannerPlan removeImpl(BatchPlannerPlan batchPlannerPlan) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(batchPlannerPlan)) {
				batchPlannerPlan = (BatchPlannerPlan)session.get(
					BatchPlannerPlanImpl.class,
					batchPlannerPlan.getPrimaryKeyObj());
			}

			if (batchPlannerPlan != null) {
				session.delete(batchPlannerPlan);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (batchPlannerPlan != null) {
			clearCache(batchPlannerPlan);
		}

		return batchPlannerPlan;
	}

	@Override
	public BatchPlannerPlan updateImpl(BatchPlannerPlan batchPlannerPlan) {
		boolean isNew = batchPlannerPlan.isNew();

		if (!(batchPlannerPlan instanceof BatchPlannerPlanModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(batchPlannerPlan.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					batchPlannerPlan);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in batchPlannerPlan proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom BatchPlannerPlan implementation " +
					batchPlannerPlan.getClass());
		}

		BatchPlannerPlanModelImpl batchPlannerPlanModelImpl =
			(BatchPlannerPlanModelImpl)batchPlannerPlan;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (batchPlannerPlan.getCreateDate() == null)) {
			if (serviceContext == null) {
				batchPlannerPlan.setCreateDate(date);
			}
			else {
				batchPlannerPlan.setCreateDate(
					serviceContext.getCreateDate(date));
			}
		}

		if (!batchPlannerPlanModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				batchPlannerPlan.setModifiedDate(date);
			}
			else {
				batchPlannerPlan.setModifiedDate(
					serviceContext.getModifiedDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(batchPlannerPlan);
			}
			else {
				batchPlannerPlan = (BatchPlannerPlan)session.merge(
					batchPlannerPlan);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(
			BatchPlannerPlanImpl.class, batchPlannerPlanModelImpl, false, true);

		cacheUniqueFindersCache(batchPlannerPlanModelImpl);

		if (isNew) {
			batchPlannerPlan.setNew(false);
		}

		batchPlannerPlan.resetOriginalValues();

		return batchPlannerPlan;
	}

	/**
	 * Returns the batch planner plan with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the batch planner plan
	 * @return the batch planner plan
	 * @throws NoSuchPlanException if a batch planner plan with the primary key could not be found
	 */
	@Override
	public BatchPlannerPlan findByPrimaryKey(Serializable primaryKey)
		throws NoSuchPlanException {

		BatchPlannerPlan batchPlannerPlan = fetchByPrimaryKey(primaryKey);

		if (batchPlannerPlan == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchPlanException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return batchPlannerPlan;
	}

	/**
	 * Returns the batch planner plan with the primary key or throws a <code>NoSuchPlanException</code> if it could not be found.
	 *
	 * @param batchPlannerPlanId the primary key of the batch planner plan
	 * @return the batch planner plan
	 * @throws NoSuchPlanException if a batch planner plan with the primary key could not be found
	 */
	@Override
	public BatchPlannerPlan findByPrimaryKey(long batchPlannerPlanId)
		throws NoSuchPlanException {

		return findByPrimaryKey((Serializable)batchPlannerPlanId);
	}

	/**
	 * Returns the batch planner plan with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param batchPlannerPlanId the primary key of the batch planner plan
	 * @return the batch planner plan, or <code>null</code> if a batch planner plan with the primary key could not be found
	 */
	@Override
	public BatchPlannerPlan fetchByPrimaryKey(long batchPlannerPlanId) {
		return fetchByPrimaryKey((Serializable)batchPlannerPlanId);
	}

	/**
	 * Returns all the batch planner plans.
	 *
	 * @return the batch planner plans
	 */
	@Override
	public List<BatchPlannerPlan> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the batch planner plans.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>BatchPlannerPlanModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of batch planner plans
	 * @param end the upper bound of the range of batch planner plans (not inclusive)
	 * @return the range of batch planner plans
	 */
	@Override
	public List<BatchPlannerPlan> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the batch planner plans.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>BatchPlannerPlanModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of batch planner plans
	 * @param end the upper bound of the range of batch planner plans (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of batch planner plans
	 */
	@Override
	public List<BatchPlannerPlan> findAll(
		int start, int end,
		OrderByComparator<BatchPlannerPlan> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the batch planner plans.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>BatchPlannerPlanModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of batch planner plans
	 * @param end the upper bound of the range of batch planner plans (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of batch planner plans
	 */
	@Override
	public List<BatchPlannerPlan> findAll(
		int start, int end,
		OrderByComparator<BatchPlannerPlan> orderByComparator,
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

		List<BatchPlannerPlan> list = null;

		if (useFinderCache) {
			list = (List<BatchPlannerPlan>)finderCache.getResult(
				finderPath, finderArgs);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_BATCHPLANNERPLAN);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_BATCHPLANNERPLAN;

				sql = sql.concat(BatchPlannerPlanModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<BatchPlannerPlan>)QueryUtil.list(
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
	 * Removes all the batch planner plans from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (BatchPlannerPlan batchPlannerPlan : findAll()) {
			remove(batchPlannerPlan);
		}
	}

	/**
	 * Returns the number of batch planner plans.
	 *
	 * @return the number of batch planner plans
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_BATCHPLANNERPLAN);

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
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "batchPlannerPlanId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_BATCHPLANNERPLAN;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return BatchPlannerPlanModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the batch planner plan persistence.
	 */
	@Activate
	public void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_argumentsResolverServiceRegistration = _bundleContext.registerService(
			ArgumentsResolver.class,
			new BatchPlannerPlanModelArgumentsResolver(),
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

		_finderPathWithPaginationFindByCompanyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCompanyId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"companyId"}, true);

		_finderPathWithoutPaginationFindByCompanyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCompanyId",
			new String[] {Long.class.getName()}, new String[] {"companyId"},
			true);

		_finderPathCountByCompanyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCompanyId",
			new String[] {Long.class.getName()}, new String[] {"companyId"},
			false);

		_finderPathWithPaginationFindByC_U = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByC_U",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"companyId", "userId"}, true);

		_finderPathWithoutPaginationFindByC_U = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByC_U",
			new String[] {Long.class.getName(), Long.class.getName()},
			new String[] {"companyId", "userId"}, true);

		_finderPathCountByC_U = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_U",
			new String[] {Long.class.getName(), Long.class.getName()},
			new String[] {"companyId", "userId"}, false);

		_finderPathFetchByC_N = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByC_N",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"companyId", "name"}, true);

		_finderPathCountByC_N = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_N",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"companyId", "name"}, false);
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(BatchPlannerPlanImpl.class.getName());

		_argumentsResolverServiceRegistration.unregister();
	}

	@Override
	@Reference(
		target = BatchPlannerPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
	}

	@Override
	@Reference(
		target = BatchPlannerPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = BatchPlannerPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	private BundleContext _bundleContext;

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_BATCHPLANNERPLAN =
		"SELECT batchPlannerPlan FROM BatchPlannerPlan batchPlannerPlan";

	private static final String _SQL_SELECT_BATCHPLANNERPLAN_WHERE =
		"SELECT batchPlannerPlan FROM BatchPlannerPlan batchPlannerPlan WHERE ";

	private static final String _SQL_COUNT_BATCHPLANNERPLAN =
		"SELECT COUNT(batchPlannerPlan) FROM BatchPlannerPlan batchPlannerPlan";

	private static final String _SQL_COUNT_BATCHPLANNERPLAN_WHERE =
		"SELECT COUNT(batchPlannerPlan) FROM BatchPlannerPlan batchPlannerPlan WHERE ";

	private static final String _FILTER_ENTITY_TABLE_FILTER_PK_COLUMN =
		"batchPlannerPlan.batchPlannerPlanId";

	private static final String _FILTER_SQL_SELECT_BATCHPLANNERPLAN_WHERE =
		"SELECT DISTINCT {batchPlannerPlan.*} FROM BatchPlannerPlan batchPlannerPlan WHERE ";

	private static final String
		_FILTER_SQL_SELECT_BATCHPLANNERPLAN_NO_INLINE_DISTINCT_WHERE_1 =
			"SELECT {BatchPlannerPlan.*} FROM (SELECT DISTINCT batchPlannerPlan.batchPlannerPlanId FROM BatchPlannerPlan batchPlannerPlan WHERE ";

	private static final String
		_FILTER_SQL_SELECT_BATCHPLANNERPLAN_NO_INLINE_DISTINCT_WHERE_2 =
			") TEMP_TABLE INNER JOIN BatchPlannerPlan ON TEMP_TABLE.batchPlannerPlanId = BatchPlannerPlan.batchPlannerPlanId";

	private static final String _FILTER_SQL_COUNT_BATCHPLANNERPLAN_WHERE =
		"SELECT COUNT(DISTINCT batchPlannerPlan.batchPlannerPlanId) AS COUNT_VALUE FROM BatchPlannerPlan batchPlannerPlan WHERE ";

	private static final String _FILTER_ENTITY_ALIAS = "batchPlannerPlan";

	private static final String _FILTER_ENTITY_TABLE = "BatchPlannerPlan";

	private static final String _ORDER_BY_ENTITY_ALIAS = "batchPlannerPlan.";

	private static final String _ORDER_BY_ENTITY_TABLE = "BatchPlannerPlan.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No BatchPlannerPlan exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No BatchPlannerPlan exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		BatchPlannerPlanPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"active"});

	@Override
	protected FinderCache getFinderCache() {
		return finderCache;
	}

	private ServiceRegistration<ArgumentsResolver>
		_argumentsResolverServiceRegistration;

	private static class BatchPlannerPlanModelArgumentsResolver
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

			BatchPlannerPlanModelImpl batchPlannerPlanModelImpl =
				(BatchPlannerPlanModelImpl)baseModel;

			long columnBitmask = batchPlannerPlanModelImpl.getColumnBitmask();

			if (!checkColumn || (columnBitmask == 0)) {
				return _getValue(
					batchPlannerPlanModelImpl, columnNames, original);
			}

			Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
				finderPath);

			if (finderPathColumnBitmask == null) {
				finderPathColumnBitmask = 0L;

				for (String columnName : columnNames) {
					finderPathColumnBitmask |=
						batchPlannerPlanModelImpl.getColumnBitmask(columnName);
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
					batchPlannerPlanModelImpl, columnNames, original);
			}

			return null;
		}

		@Override
		public String getClassName() {
			return BatchPlannerPlanImpl.class.getName();
		}

		@Override
		public String getTableName() {
			return BatchPlannerPlanTable.INSTANCE.getTableName();
		}

		private static Object[] _getValue(
			BatchPlannerPlanModelImpl batchPlannerPlanModelImpl,
			String[] columnNames, boolean original) {

			Object[] arguments = new Object[columnNames.length];

			for (int i = 0; i < arguments.length; i++) {
				String columnName = columnNames[i];

				if (original) {
					arguments[i] =
						batchPlannerPlanModelImpl.getColumnOriginalValue(
							columnName);
				}
				else {
					arguments[i] = batchPlannerPlanModelImpl.getColumnValue(
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

			orderByColumnsBitmask |= BatchPlannerPlanModelImpl.getColumnBitmask(
				"modifiedDate");

			_ORDER_BY_COLUMNS_BITMASK = orderByColumnsBitmask;
		}

	}

}