package s4y.itag.ble.internal;

import android.os.Handler;
import android.os.Looper;

import s4y.itag.ble.BLEConnectionsInterface;
import s4y.itag.ble.BLEScannerInterface;
import s4y.itag.ble.observables.Subject;
import s4y.itag.ble.observables.SubjectNext;

public class BLEScannerDefault implements BLEScannerInterface {
    private final BLEManagerInterface manager;
    private final BLEConnectionsInterface connections;
    private final SubjectNext<Integer> subjectTimer = new SubjectNext<>();
    private final Handler handlerTimer = new Handler(Looper.getMainLooper());
    private int timeout = 0;
    private final Runnable runnableTimer = new Runnable() {
        @Override
        public void run() {
            timeout --;
            subjectTimer.onNext(timeout);
            if (timeout <= 0) {
                handlerTimer.removeCallbacks(this);
            }else{
                handlerTimer.postDelayed(this, 1000);
            }
        }
    };

    BLEScannerDefault(BLEConnectionsInterface connections, BLEManagerInterface manager){
        this.manager = manager;
        this.connections = connections;
    }

    @Override
    public boolean isScanning() {
        return manager.isScanning();
    }

    @Override
    public int scanningTimeout() {
        return timeout;
    }

    @Override
    public Subject<Integer> getTimerSubject() {
        return subjectTimer;
    }

    @Override
    public Subject<BLEDiscoveryResult> getDiscoverySubject() {
        return manager.getObservables().getDidDiscoverPeripheral();
    }

    @Override
    public void start(int timeout, String[] forceCancelIds) {
        stop();
        if (!manager.canScan())
            return;
        for (String id: forceCancelIds){
           connections.disconnect(id);
        }
        this.timeout = timeout;
        manager.scanForPeripherals();
        handlerTimer.postDelayed(runnableTimer, 1000);
    }

    @Override
    public void stop() {
        manager.stopScan();
    }
}