//package com.example.wrapfluttermodule.ListWidget;
//
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.widget.RemoteViewsService;
//
//public class ListWidgetService extends RemoteViewsService {
//    @Override
//    public RemoteViewsFactory onGetViewFactory(Intent intent) {
//        return null;
//    }
//}
//
//class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
//    private Context mContext;
//    private Cursor mCursor;
//    private  int mAppWidgetId;
//
//    public ListRemoteViewsFactory(Context context, Intent  intent) {
//        mContext =  context;
//        mAppWidgetId = intent.getIntExtra()
//    }
//}