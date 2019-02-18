package com.caixp;


import com.sean.init.utils.JSONUtils;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import rx.Observable;
import rx.internal.util.InternalObservableUtils;

public class CommonTests {

    @Test
    public void test() {
        Observable<Long> interval = Observable.interval(50, TimeUnit.MILLISECONDS);

        int i = RandomUtils.nextInt(2, 2);
        System.out.println("Hello World!"+i);
    }

    @Test
    public void timeWindowTest() throws Exception{
        Observable<Integer> source = Observable.interval(50, TimeUnit.MILLISECONDS).map(i -> RandomUtils.nextInt(0,2));
        source.window(1, TimeUnit.SECONDS).subscribe(window -> {
            int[] metrics = new int[2];
            window.subscribe(
                    i ->
                            metrics[i]++,
                    InternalObservableUtils.ERROR_NOT_IMPLEMENTED,
                    () ->
                            System.out.println("窗口Metrics:" + JSONUtils.writeValue(metrics)));
        });
        TimeUnit.SECONDS.sleep(3);
    }
}
