package com.example.xiaochengshu;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.SearchParam;
import com.tencent.lbssearch.object.result.SearchResultObject;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

public class SearchPosition {
    /**
     * poi检索
     */
    static public void searchPoi(AppCompatActivity activity, String positionText, TencentMap tencentMap) {
        TencentSearch tencentSearch = new TencentSearch(activity);
        String keyWord = positionText.trim();
        SearchParam.Rectangle rectangle = new SearchParam.Rectangle(new LatLng(111,222),new LatLng(666,777));



        //构建地点检索
        SearchParam searchParam = new SearchParam(keyWord, rectangle);
        tencentSearch.search(searchParam, new HttpResponseListener<BaseObject>() {

            @Override
            public void onFailure(int arg0, String arg2,
                                  Throwable arg3) {
                Toast.makeText(activity.getApplicationContext(), arg2, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int arg0, BaseObject arg1) {
                if (arg1 == null) {
                    return;
                }
                SearchResultObject obj = (SearchResultObject) arg1;
                if(obj.data == null){
                    return;
                }
                //将地图中心坐标移动到检索到的第一个地点
                tencentMap.moveCamera(CameraUpdateFactory
                        .newCameraPosition(
                                new CameraPosition(obj.data.get(0).latLng,
                                        //设置缩放级别到 15 级
                                        15f,
                                        0,
                                        0)));
                //将其他检索到的地点在地图上用 marker 标出来
                for(SearchResultObject.SearchResultData data : obj.data){
                    Log.v("SearchDemo","title:"+data.title + ";" + data.address);
                    tencentMap.addMarker(new MarkerOptions()
                            //标注的位置
                            .position(data.latLng)
                            //标注的InfoWindow的标题
                            .title(data.title)
                            //标注的InfoWindow的内容
                            .snippet(data.address)
                    );

                }
            }
        });
    }
}
