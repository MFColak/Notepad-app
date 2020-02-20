package com.notepad;

import android.content.Context;

public class whatTimeIsIt {

    private static final int saniye = 1000;
    private static final int dakika = 60*saniye;
    private static final int saat = 60*dakika;
    private static final int gun = 24*saat;


    public static String whattime(long time, Context cntx){
        if (time<1000000000000L){
            time*=1000;
        }

        long suan = System.currentTimeMillis();
        if (time > suan || time <=0){
            return null;
        }
        final long fark = suan-time;

        if (fark < dakika){
            return "simdi";
        }else if (fark < 2*dakika){
            return "bir dakika önce";
        }else if (fark < 50*dakika){
            return (fark/dakika+"dakika önce");
        }else if (fark < 90*dakika){
            return "bir saat önce";
        }else if (fark < 24*saat){
            return (fark/saat+"saat önce");
        }else if (fark < 48*saat){
            return "bir gün önce";
        }else {
            return fark/gun+"gün önce";
        }

    }
}
