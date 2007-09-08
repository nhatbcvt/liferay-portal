/**
 * Copyright (c) 2000-2007 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portlet.calendar.service.http;

import com.liferay.portlet.calendar.model.CalEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * <a href="CalEventJSONSerializer.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by <code>com.liferay.portlet.calendar.service.http.CalEventServiceJSON</code>
 * to translate objects.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.calendar.service.http.CalEventServiceJSON
 *
 */
public class CalEventJSONSerializer {
	public static JSONObject toJSONObject(CalEvent model) {
		JSONObject jsonObj = new JSONObject();
		JSONUtil.put(jsonObj, "eventId", model.getEventId());
		JSONUtil.put(jsonObj, "groupId", model.getGroupId());
		JSONUtil.put(jsonObj, "companyId", model.getCompanyId());
		JSONUtil.put(jsonObj, "userId", model.getUserId());
		JSONUtil.put(jsonObj, "userName", model.getUserName());
		JSONUtil.put(jsonObj, "createDate", model.getCreateDate());
		JSONUtil.put(jsonObj, "modifiedDate", model.getModifiedDate());
		JSONUtil.put(jsonObj, "title", model.getTitle());
		JSONUtil.put(jsonObj, "description", model.getDescription());
		JSONUtil.put(jsonObj, "startDate", model.getStartDate());
		JSONUtil.put(jsonObj, "endDate", model.getEndDate());
		JSONUtil.put(jsonObj, "durationHour", model.getDurationHour());
		JSONUtil.put(jsonObj, "durationMinute", model.getDurationMinute());
		JSONUtil.put(jsonObj, "allDay", model.getAllDay());
		JSONUtil.put(jsonObj, "timeZoneSensitive", model.getTimeZoneSensitive());
		JSONUtil.put(jsonObj, "type", model.getType());
		JSONUtil.put(jsonObj, "repeating", model.getRepeating());
		JSONUtil.put(jsonObj, "recurrence", model.getRecurrence());
		JSONUtil.put(jsonObj, "remindBy", model.getRemindBy());
		JSONUtil.put(jsonObj, "firstReminder", model.getFirstReminder());
		JSONUtil.put(jsonObj, "secondReminder", model.getSecondReminder());

		return jsonObj;
	}

	public static JSONArray toJSONArray(List models) {
		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < models.size(); i++) {
			CalEvent model = (CalEvent)models.get(i);
			jsonArray.put(toJSONObject(model));
		}

		return jsonArray;
	}
}