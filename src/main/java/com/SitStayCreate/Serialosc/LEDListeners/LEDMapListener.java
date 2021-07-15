package com.SitStayCreate.Serialosc.LEDListeners;

import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.OSCMessageListener;

import java.util.List;

public abstract class LEDMapListener implements OSCMessageListener {

    @Override
    public void acceptMessage(OSCMessageEvent event) {
        //There will be 10 args xOffset, yOffset (determines which 8x8 quad is being lit)
        // then 8 additional args. Each additional arg is a bitmask of a row
        // (0-255, 0-255, 0-255, 0-255, 0-255, 0-255, 0-255, 0-255)
        //This is similar to LedRow and LedCol, but with 8 values instead of 1.
        List oscArgList = event.getMessage().getArguments();
        int xOffset = (int) oscArgList.get(0);
        int yOffset = (int) oscArgList.get(1);

        for(int i = 2; i < oscArgList.size(); i++){
            //bitMask = a row of values 00000000 is all off 11111111 is all on, 00001111 is 1st 4 off 2nd 4 on
            int bitMask = (int) oscArgList.get(i);
            //binaryString does not have leading 0s, i.e. if bitmask = 10, binaryString = 1010, not 00001010
            String binaryString = Integer.toBinaryString(bitMask);
            setLEDMap(binaryString, xOffset, yOffset, i-2);
        }
    }

    public abstract void setLEDMap(String binaryString, int xOffset, int yOffset, int yCounter);
}
