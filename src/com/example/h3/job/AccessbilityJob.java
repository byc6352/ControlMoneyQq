/**
 * 
 */
package com.example.h3.job;
import android.view.accessibility.AccessibilityEvent;

import com.example.h3.QiangHongBaoService;
/**
 * @author byc
 *
 */
public interface AccessbilityJob {

    void onCreateJob(QiangHongBaoService service);
    void onReceiveJob(AccessibilityEvent event);
    void onStopJob();
}
