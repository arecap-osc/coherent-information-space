package org.hkrdi.eden.ggm.algebraic.netting.compute.index;

@Deprecated
public final class VertexIndex {

    public static int getBeltIndex(int networkIndex) {
        for(int i = 0; i <= networkIndex; i++){
            if(networkIndex > ModIndex.getMod6Index0(i)) {
                continue;
            }
            return i;
        }
        return 0;
    }

    public static int getEdgeLastIndex(int networkIndex) {
        return getEdgeLastIndex(networkIndex, getBeltIndex(networkIndex));
    }

    public static int getEdgeLastIndex(int networkIndex, int beltIndex) {
        int lastIndex = 12 + 6 * networkIndex;
        int counted = 1;
        if(networkIndex > 0) {
            lastIndex++;
        }
        if(networkIndex < ModIndex.getMod6Index1(beltIndex)) {
            counted += 6 * (beltIndex -1);
            lastIndex += 18 *  (beltIndex - 1);
        } else if(networkIndex < ModIndex.getMod6Index2(beltIndex)) {
            counted += 6 * (beltIndex -1) + 1;
            lastIndex += 3 * (6 * (beltIndex -1) + 1);
        } else if(networkIndex < ModIndex.getMod6Index3(beltIndex)) {
            counted += 6 * (beltIndex -1) + 2;
            lastIndex += 3 * (6 * (beltIndex -1) + 2);
        } else if(networkIndex < ModIndex.getMod6Index4(beltIndex)) {
            counted += 6 * (beltIndex -1) + 3;
            lastIndex += 3 * (6 * (beltIndex -1) + 3);
        } else if(networkIndex < ModIndex.getMod6Index5(beltIndex)) {
            counted += 6 * (beltIndex -1) + 4;
            lastIndex += 3 * (6 * (beltIndex -1) + 4);
        } else if(beltIndex > 0 && networkIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            counted += 6 * (beltIndex -1) + 5;
            lastIndex += 3 * (6 * (beltIndex -1) + 5);
        }
        if(networkIndex + 1 - counted > 0) {
            lastIndex += 2 * (networkIndex + 1 - counted);
        }
        return lastIndex;
    }

    public static int getVertexLastIndex(int networkIndex) {
        return getVertexLastIndex(networkIndex, getBeltIndex(networkIndex));
    }

    public static int getVertexLastIndex(int networkIndex, int beltIndex) {
        int lastIndex = 12 + 6 * networkIndex;
        int counted = 1;
        if(networkIndex > 0) {
            lastIndex++;
        }
        if(networkIndex < ModIndex.getMod6Index1(beltIndex)) {
            counted += 6 * (beltIndex -1);
            lastIndex += 24 *  (beltIndex - 1);
        } else if(networkIndex < ModIndex.getMod6Index2(beltIndex)) {
            counted += 6 * (beltIndex -1) + 1;
            lastIndex += 4 * (6 * (beltIndex -1) + 1);
        } else if(networkIndex < ModIndex.getMod6Index3(beltIndex)) {
            counted += 6 * (beltIndex -1) + 2;
            lastIndex += 4 * (6 * (beltIndex -1) + 2);
        } else if(networkIndex < ModIndex.getMod6Index4(beltIndex)) {
            counted += 6 * (beltIndex -1) + 3;
            lastIndex += 4 * (6 * (beltIndex -1) + 3);
        } else if(networkIndex < ModIndex.getMod6Index5(beltIndex)) {
            counted += 6 * (beltIndex -1) + 4;
            lastIndex += 4 * (6 * (beltIndex -1) + 4);
        } else if(beltIndex > 0 && networkIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            counted += 6 * (beltIndex -1) + 5;
            lastIndex += 4 * (6 * (beltIndex -1) + 5);
        }
        if(networkIndex + 1 - counted > 0) {
            lastIndex += 3 * (networkIndex + 1 - counted);
        }
        return lastIndex;
    }

    public static int getEdgeOuterSource1Index(int networkIndex) {
        int beltIndex = getBeltIndex(networkIndex);
        int lastIndex = getEdgeLastIndex(networkIndex, beltIndex);
        if(networkIndex == 0) {
            return 1;
        }
        if(networkIndex == 1) {
            return 14;
        }
        if(networkIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return lastIndex - 7;
        } else if(networkIndex < ModIndex.getMod6Index2(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index1(beltIndex) ? lastIndex - 8 :
                    getEdgeOuterSensor1Index(networkIndex - 1);
        } else if(networkIndex < ModIndex.getMod6Index3(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index2(beltIndex) ? lastIndex - 15 :
                    getEdgeOuterSensor1Index(ModIndex.getMod6Index2(beltIndex - 1) + (networkIndex - ModIndex.getMod6Index2(beltIndex) - 1));
        } else if(networkIndex < ModIndex.getMod6Index4(beltIndex)) {
            return  networkIndex == ModIndex.getMod6Index3(beltIndex) ? getEdgeOuterSensor1Index(ModIndex.getMod6Index4(beltIndex - 1)- (ModIndex.getMod6Index4(beltIndex) - networkIndex - 1)) :
                    getEdgeOuterDecider1Index(ModIndex.getMod6Index3(beltIndex - 1) + (networkIndex - ModIndex.getMod6Index3(beltIndex) - 1));
        } else if(networkIndex < ModIndex.getMod6Index5(beltIndex)) {
            return getEdgeOuterDecider1Index(ModIndex.getMod6Index5(beltIndex - 1) - (ModIndex.getMod6Index5(beltIndex) - networkIndex - 1));
        } else if(networkIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return lastIndex - 6;
        }
        return 0;
    }

    public static int getEdgeOuterSensor1Index(int networkIndex) {
        int beltIndex = getBeltIndex(networkIndex);
        int lastIndex = getEdgeLastIndex(networkIndex, beltIndex);
        if(networkIndex == 0) {
            return 3;
        }
        if(networkIndex == 1) {
            return 16;
        }
        if(networkIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return getEdgeOuterSource1Index(ModIndex.getMod6Index1(beltIndex - 1) - (ModIndex.getMod6Index1(beltIndex) - networkIndex - 1));
        } else if(networkIndex < ModIndex.getMod6Index2(beltIndex)) {
            return lastIndex - 6;
        } else if(networkIndex < ModIndex.getMod6Index3(beltIndex)) {
            return lastIndex - 7;
        } else if(networkIndex < ModIndex.getMod6Index4(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index3(beltIndex) ? lastIndex - 8 :
                    getEdgeOuterDecider1Index(networkIndex - 1);
        } else if(networkIndex < ModIndex.getMod6Index5(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index4(beltIndex) ? lastIndex - 15 :
                    getEdgeOuterSource1Index(networkIndex - 1);
        } else if(networkIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return networkIndex == ModIndex.getMod6Index5(beltIndex) ? getEdgeOuterDecider1Index(ModIndex.getMod6Index5(beltIndex-1)) :
                    getEdgeOuterSource1Index(ModIndex.getMod6Index5(beltIndex - 1) + (networkIndex - ModIndex.getMod6Index5(beltIndex) - 1));
        }
        return 0;
    }

    public static int getEdgeOuterDecider1Index(int networkIndex) {
        int beltIndex = getBeltIndex(networkIndex);
        int lastIndex = getEdgeLastIndex(networkIndex, beltIndex);
        if(networkIndex == 0) {
            return 5;
        }
        if(networkIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return  networkIndex - 1 == ModIndex.getMod6Index0(beltIndex - 1) ? lastIndex - 15 :
                    getEdgeOuterSource1Index(ModIndex.getMod6Index1(beltIndex - 1) - (ModIndex.getMod6Index1(beltIndex) - networkIndex));
        } else if(networkIndex < ModIndex.getMod6Index2(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index1(beltIndex) ? getEdgeOuterSource1Index(ModIndex.getMod6Index1(beltIndex-1)) :
                   getEdgeOuterSensor1Index(ModIndex.getMod6Index1(beltIndex-1) + (networkIndex - ModIndex.getMod6Index1(beltIndex) - 1));
        } else if(networkIndex < ModIndex.getMod6Index3(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index2(beltIndex) ? getEdgeOuterSensor1Index(ModIndex.getMod6Index2(beltIndex-1)) :
                    getEdgeOuterSensor1Index(ModIndex.getMod6Index3(beltIndex-1) - (ModIndex.getMod6Index3(beltIndex) - networkIndex - 1));
        } else if(networkIndex < ModIndex.getMod6Index4(beltIndex)) {
            return lastIndex - 6;
        } else if(networkIndex < ModIndex.getMod6Index5(beltIndex)) {
            return lastIndex - 7;
        } else if(networkIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return networkIndex == ModIndex.getMod6Index5(beltIndex) ? lastIndex - 8 :
                    getEdgeOuterSource1Index(networkIndex - 1);
        }
        return 0;
    }

    public static int getEdgeOuterSource2Index(int networkIndex) {
        int beltIndex = getBeltIndex(networkIndex);
        int lastIndex = getEdgeLastIndex(networkIndex, beltIndex);
        if(networkIndex == 0) {
            return 4;
        }
        if(networkIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return  networkIndex - 1 == ModIndex.getMod6Index0(beltIndex - 1) ? getEdgeOuterSensor2Index(ModIndex.getMod6Index1(beltIndex-1) - (ModIndex.getMod6Index1(beltIndex) - networkIndex - 1)) :
                    getEdgeOuterDecider2Index(ModIndex.getMod6Index1(beltIndex - 1) - (ModIndex.getMod6Index1(beltIndex) - networkIndex));
        } else if(networkIndex < ModIndex.getMod6Index2(beltIndex)) {
            return getEdgeOuterDecider2Index(ModIndex.getMod6Index1(beltIndex-1) + (networkIndex - ModIndex.getMod6Index1(beltIndex)));
        } else if(networkIndex < ModIndex.getMod6Index3(beltIndex)) {
            return lastIndex - 6;
        } else if(networkIndex < ModIndex.getMod6Index4(beltIndex)) {
            return lastIndex - 7;
        } else if(networkIndex < ModIndex.getMod6Index5(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index4(beltIndex) ? lastIndex - 8 :
                    getEdgeOuterSensor2Index(networkIndex - 1);
        } else if(networkIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return  networkIndex == ModIndex.getMod6Index5(beltIndex) ? getEdgeOuterSensor2Index(networkIndex - 1) :
                    getEdgeOuterSensor2Index(ModIndex.getMod6Index5(beltIndex-1) + (networkIndex - ModIndex.getMod6Index5(beltIndex) - 1));
        }
        return 0;
    }

    public static int getEdgeOuterSensor2Index(int networkIndex) {
        int beltIndex = getBeltIndex(networkIndex);
        int lastIndex = getEdgeLastIndex(networkIndex, beltIndex);
        if(networkIndex == 0) {
            return 6;
        }
        if(networkIndex == 1) {
            return 13;
        }
        if(networkIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return networkIndex - 1 == ModIndex.getMod6Index0(beltIndex - 1) ? lastIndex - 8 :
                    getEdgeOuterDecider2Index(networkIndex - 1);
        } else if(networkIndex < ModIndex.getMod6Index2(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index1(beltIndex) ? getEdgeOuterDecider2Index(networkIndex - 1) :
                    getEdgeOuterDecider2Index(ModIndex.getMod6Index1(beltIndex-1) + (networkIndex - ModIndex.getMod6Index1(beltIndex) - 1));
        } else if(networkIndex < ModIndex.getMod6Index3(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index2(beltIndex) ? getEdgeOuterDecider2Index(ModIndex.getMod6Index2(beltIndex - 1)) :
                    getEdgeOuterSource2Index(ModIndex.getMod6Index2(beltIndex-1) + (networkIndex - ModIndex.getMod6Index2(beltIndex) - 1));
        } else if(networkIndex < ModIndex.getMod6Index4(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index3(beltIndex) ? getEdgeOuterSource2Index(ModIndex.getMod6Index3(beltIndex-1)) :
                    getEdgeOuterSource2Index(ModIndex.getMod6Index4(beltIndex - 1) - (ModIndex.getMod6Index4(beltIndex) - networkIndex - 1));
        } else if(networkIndex < ModIndex.getMod6Index5(beltIndex)) {
            return lastIndex - 6;
        } else if(networkIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return lastIndex - 7;
        }
        return 0;
    }

    public static int getEdgeOuterDecider2Index(int networkIndex) {
        int beltIndex = getBeltIndex(networkIndex);
        int lastIndex = getEdgeLastIndex(networkIndex, beltIndex);
        if(networkIndex == 0) {
            return 2;
        }
        if(networkIndex == 1) {
            return 15;
        }
        if(networkIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return lastIndex - 6;
        } else if(networkIndex < ModIndex.getMod6Index2(beltIndex)) {
            return lastIndex - 7;
        } else if(networkIndex < ModIndex.getMod6Index3(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index2(beltIndex) ? lastIndex - 8 :
                    getEdgeOuterSource2Index(networkIndex - 1);
        } else if(networkIndex < ModIndex.getMod6Index4(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index3(beltIndex) ? getEdgeOuterSource2Index(networkIndex-1) :
                    getEdgeOuterSource2Index(ModIndex.getMod6Index3(beltIndex - 1) + (networkIndex - ModIndex.getMod6Index3(beltIndex) - 1));
        } else if(networkIndex < ModIndex.getMod6Index5(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index4(beltIndex) ? getEdgeOuterSensor2Index(networkIndex - 1) :
                    getEdgeOuterSensor2Index(ModIndex.getMod6Index4(beltIndex - 1) + (networkIndex - ModIndex.getMod6Index4(beltIndex) - 1));
        } else if(networkIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return getEdgeOuterSensor2Index(ModIndex.getMod6Index5(beltIndex - 1) + (networkIndex - ModIndex.getMod6Index5(beltIndex)));
        }
        return 0;
    }


    public static int getVertexOuterSource1Index(int networkIndex) {
        int beltIndex = getBeltIndex(networkIndex);
        int lastIndex = getVertexLastIndex(networkIndex, beltIndex);
        if(networkIndex == 0) {
            return 1;
        }
        if(networkIndex == 1) {
            return 15;
        }
        if(networkIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return lastIndex - 7;
        } else if(networkIndex < ModIndex.getMod6Index2(beltIndex)) {
            return lastIndex - 8;
        } else if(networkIndex < ModIndex.getMod6Index3(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index2(beltIndex) ? lastIndex - 9 :
                    getVertexOuterSource2Index(networkIndex - 1);
        } else if(networkIndex < ModIndex.getMod6Index4(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index3(beltIndex) ? getVertexOuterSource2Index(networkIndex - 1) :
                    getVertexOuterSource2Index(ModIndex.getMod6Index3(beltIndex - 1) + (networkIndex - ModIndex.getMod6Index3(beltIndex) - 1));
        } else if(networkIndex < ModIndex.getMod6Index5(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index4(beltIndex) ? getVertexOuterSource2Index(ModIndex.getMod6Index4(beltIndex - 1)) :
                    getVertexOuterSource2Index(ModIndex.getMod6Index5(beltIndex - 1) - (ModIndex.getMod6Index5(beltIndex) - networkIndex - 1));
        } else if(networkIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return lastIndex - 6;
        }
        return 0;
    }

    public static int getVertexOuterSensor1Index(int networkIndex) {
        int beltIndex = getBeltIndex(networkIndex);
        int lastIndex = getVertexLastIndex(networkIndex, beltIndex);
        if(networkIndex == 0) {
            return 3;
        }
        if(networkIndex == 1) {
            return 17;
        }
        if(networkIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return getVertexOuterSensor2Index(ModIndex.getMod6Index1(beltIndex - 1) - (ModIndex.getMod6Index1(beltIndex) - networkIndex - 1));
        } else if(networkIndex < ModIndex.getMod6Index2(beltIndex)) {
            return lastIndex - 6;
        } else if(networkIndex < ModIndex.getMod6Index3(beltIndex)) {
            return lastIndex - 7;
        } else if(networkIndex < ModIndex.getMod6Index4(beltIndex)) {
            return lastIndex - 8;
        } else if(networkIndex < ModIndex.getMod6Index5(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index4(beltIndex) ? lastIndex - 9 : getVertexOuterSensor2Index(networkIndex - 1);
        } else if(networkIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return networkIndex == ModIndex.getMod6Index5(beltIndex) ? getVertexOuterSensor2Index(networkIndex - 1) :
                    getVertexOuterSensor2Index(ModIndex.getMod6Index5(beltIndex - 1) + (networkIndex - ModIndex.getMod6Index5(beltIndex) - 1));
        }
        return 0;
    }

    public static int getVertexOuterDecider1Index(int networkIndex) {
        int beltIndex = getBeltIndex(networkIndex);
        int lastIndex = getVertexLastIndex(networkIndex, beltIndex);
        if(networkIndex == 0) {
            return 5;
        }
        if(networkIndex == 1) {
            return 13;
        }
        if(networkIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return networkIndex - 1 == ModIndex.getMod6Index0(beltIndex - 1) ? lastIndex - 9 :
                    getVertexOuterDecider2Index(networkIndex - 1);
        } else if(networkIndex < ModIndex.getMod6Index2(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index1(beltIndex) ? getVertexOuterDecider2Index(networkIndex - 1) :
                    getVertexOuterDecider2Index(ModIndex.getMod6Index1(beltIndex - 1) + (networkIndex - ModIndex.getMod6Index1(beltIndex) - 1));
        } else if(networkIndex < ModIndex.getMod6Index3(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index2(beltIndex) ? getVertexOuterDecider2Index(beltIndex - 1) :
                    getVertexOuterDecider2Index(ModIndex.getMod6Index3(beltIndex - 1) - (ModIndex.getMod6Index3(beltIndex) - networkIndex - 1));
        } else if(networkIndex < ModIndex.getMod6Index4(beltIndex)) {
            return lastIndex - 6;
        } else if(networkIndex < ModIndex.getMod6Index5(beltIndex)) {
            return lastIndex - 7;
        } else if(networkIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return lastIndex - 8;
        }
        return 0;
    }

    public static int getVertexOuterSource2Index(int networkIndex) {
        int beltIndex = getBeltIndex(networkIndex);
        int lastIndex = getVertexLastIndex(networkIndex, beltIndex);
        if(networkIndex == 0) {
            return 4;
        }
        if(networkIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return networkIndex - 1 == ModIndex.getMod6Index0(beltIndex - 1) ? getVertexOuterSource1Index(networkIndex -1) :
                    getVertexOuterSource1Index(ModIndex.getMod6Index1(beltIndex - 1) - (ModIndex.getMod6Index1(beltIndex) - networkIndex));
        } else if(networkIndex < ModIndex.getMod6Index2(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index1(beltIndex) ? getVertexOuterSource1Index(ModIndex.getMod6Index1(beltIndex - 1)) :
                    getVertexOuterSource1Index(ModIndex.getMod6Index2(beltIndex - 1) - (ModIndex.getMod6Index2(beltIndex) - networkIndex - 1));
        } else if(networkIndex < ModIndex.getMod6Index3(beltIndex)) {
            return lastIndex - 6;
        } else if(networkIndex < ModIndex.getMod6Index4(beltIndex)) {
            return lastIndex - 7;
        } else if(networkIndex < ModIndex.getMod6Index5(beltIndex)) {
            return lastIndex - 8;
        } else if(networkIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return  networkIndex == ModIndex.getMod6Index5(beltIndex) ? lastIndex - 9 :
                    getVertexOuterSource1Index(networkIndex - 1);
        }
        return 0;
    }

    public static int getVertexOuterSensor2Index(int networkIndex) {
        int beltIndex = getBeltIndex(networkIndex);
        int lastIndex = getVertexLastIndex(networkIndex, beltIndex);
        if(networkIndex == 0) {
            return 6;
        }
        if(networkIndex == 1) {
            return 14;
        }
        if(networkIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return lastIndex - 8;
        } else if(networkIndex < ModIndex.getMod6Index2(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index1(beltIndex) ? lastIndex - 9 :
                    getVertexOuterSensor1Index(networkIndex - 1);
        } else if(networkIndex < ModIndex.getMod6Index3(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index2(beltIndex) ? getVertexOuterSensor1Index(networkIndex - 1) :
                    getVertexOuterSensor1Index(ModIndex.getMod6Index2(beltIndex - 1) + (networkIndex - ModIndex.getMod6Index2(beltIndex) - 1));
        } else if(networkIndex < ModIndex.getMod6Index4(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index3(beltIndex) ? getVertexOuterSensor1Index(ModIndex.getMod6Index3(beltIndex - 1)) :
                    getVertexOuterSensor1Index(ModIndex.getMod6Index4(beltIndex - 1) - (ModIndex.getMod6Index4(beltIndex) - networkIndex - 1));
        } else if(networkIndex < ModIndex.getMod6Index5(beltIndex)) {
            return lastIndex - 6;
        } else if(networkIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return lastIndex - 7;
        }
        return 0;
    }

    public static int getVertexOuterDecider2Index(int networkIndex) {
        int beltIndex = getBeltIndex(networkIndex);
        int lastIndex = getVertexLastIndex(networkIndex, beltIndex);
        if(networkIndex == 0) {
            return 2;
        }
        if(networkIndex == 1) {
            return 16;
        }
        if(networkIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return lastIndex - 6;
        } else if(networkIndex < ModIndex.getMod6Index2(beltIndex)) {
            return lastIndex - 7;
        } else if(networkIndex < ModIndex.getMod6Index3(beltIndex)) {
            return lastIndex - 8;
        } else if(networkIndex < ModIndex.getMod6Index4(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index3(beltIndex) ? lastIndex - 9 :
                    getVertexOuterDecider1Index(networkIndex - 1);
        } else if(networkIndex < ModIndex.getMod6Index5(beltIndex)) {
            return networkIndex == ModIndex.getMod6Index4(beltIndex) ? getVertexOuterDecider1Index(networkIndex - 1) :
                    getVertexOuterDecider1Index(ModIndex.getMod6Index4(beltIndex - 1) + (networkIndex - ModIndex.getMod6Index4(beltIndex) - 1));
        } else if(networkIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return networkIndex == ModIndex.getMod6Index5(beltIndex) ? getVertexOuterDecider1Index(ModIndex.getMod6Index5(beltIndex -1)) :
                    getVertexOuterDecider1Index(ModIndex.getMod6Index1(beltIndex) - (beltIndex - 1) - (ModIndex.getMod6Index0(beltIndex) - networkIndex));
        }
        return 0;
    }

}
