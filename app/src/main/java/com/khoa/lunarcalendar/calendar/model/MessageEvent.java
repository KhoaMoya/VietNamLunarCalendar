package com.khoa.lunarcalendar.calendar.model;

public class MessageEvent {

    public static enum Action{
        SHOW_DETAIL_EVENT_FRAGMNET,
        SHOW_EDIT_EVENT_ACTIVITY,
        SHOW_ADD_EVENT_ACTIVITY,

        EDITED_EVENT,
        DELETED_EVENT,

        UPDATE_TEXT_EVENT_LIST_DAY_SIZE,
        UPDATE_TEXT_EVENT_LIST_MONTH_SIZE,

        DAY_CLICK,

        SCROLL_EVENT_LIST
    }

    public Action action;
    public Object data;

    public MessageEvent(Action action, Object data) {
        this.action = action;
        this.data = data;
    }
}
