package org.hkrdi.eden.ggm.algebraic.netting;

import org.hkrdi.eden.ggm.algebraic.netting.compute.index.ModIndex;

public interface EdgesNetwork extends Network {

    @Override
    default int getLastIndex(int spaceIndex) {
        int beltIndex = getBeltIndex(spaceIndex);
        int lastIndex = 12 + 6 * spaceIndex;
        int counted = 1;
        if(spaceIndex > 0) {
            lastIndex++;
        }
        if(spaceIndex < ModIndex.getMod6Index1(beltIndex)) {
            counted += 6 * (beltIndex -1);
            lastIndex += 18 *  (beltIndex - 1);
        } else if(spaceIndex < ModIndex.getMod6Index2(beltIndex)) {
            counted += 6 * (beltIndex -1) + 1;
            lastIndex += 3 * (6 * (beltIndex -1) + 1);
        } else if(spaceIndex < ModIndex.getMod6Index3(beltIndex)) {
            counted += 6 * (beltIndex -1) + 2;
            lastIndex += 3 * (6 * (beltIndex -1) + 2);
        } else if(spaceIndex < ModIndex.getMod6Index4(beltIndex)) {
            counted += 6 * (beltIndex -1) + 3;
            lastIndex += 3 * (6 * (beltIndex -1) + 3);
        } else if(spaceIndex < ModIndex.getMod6Index5(beltIndex)) {
            counted += 6 * (beltIndex -1) + 4;
            lastIndex += 3 * (6 * (beltIndex -1) + 4);
        } else if(beltIndex > 0 && spaceIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            counted += 6 * (beltIndex -1) + 5;
            lastIndex += 3 * (6 * (beltIndex -1) + 5);
        }
        if(spaceIndex + 1 - counted > 0) {
            lastIndex += 2 * (spaceIndex + 1 - counted);
        }
        return lastIndex;
    }

    default int getCognitiveOuterSourceIndex(int spaceIndex) {
        int beltIndex = getBeltIndex(spaceIndex);
        int lastIndex = getLastIndex(spaceIndex);
        if(spaceIndex == 0) {
            return 1;
        }
        if(spaceIndex == 1) {
            return 14;
        }
        if(spaceIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return lastIndex - 7;
        } else if(spaceIndex < ModIndex.getMod6Index2(beltIndex)) {
            return spaceIndex == ModIndex.getMod6Index1(beltIndex) ? lastIndex - 8 :
                    getCognitiveOuterSensorIndex(spaceIndex - 1);
        } else if(spaceIndex < ModIndex.getMod6Index3(beltIndex)) {
            return spaceIndex == ModIndex.getMod6Index2(beltIndex) ? lastIndex - 15 :
                    getCognitiveOuterSensorIndex(ModIndex.getMod6Index2(beltIndex - 1) + (spaceIndex - ModIndex.getMod6Index2(beltIndex) - 1));
        } else if(spaceIndex < ModIndex.getMod6Index4(beltIndex)) {
            return  spaceIndex == ModIndex.getMod6Index3(beltIndex) ? getCognitiveOuterSensorIndex(ModIndex.getMod6Index4(beltIndex - 1)- (ModIndex.getMod6Index4(beltIndex) - spaceIndex - 1)) :
                    getCognitiveOuterDeciderIndex(ModIndex.getMod6Index3(beltIndex - 1) + (spaceIndex - ModIndex.getMod6Index3(beltIndex) - 1));
        } else if(spaceIndex < ModIndex.getMod6Index5(beltIndex)) {
            return getCognitiveOuterDeciderIndex(ModIndex.getMod6Index5(beltIndex - 1) - (ModIndex.getMod6Index5(beltIndex) - spaceIndex - 1));
        } else if(spaceIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return lastIndex - 6;
        }
        return lastIndex - 6;
    }

    default int getCognitiveOuterSensorIndex(int spaceIndex) {
        int beltIndex = getBeltIndex(spaceIndex);
        int lastIndex = getLastIndex(spaceIndex);
        if(spaceIndex == 0) {
            return 3;
        }
        if(spaceIndex == 1) {
            return 16;
        }
        if(spaceIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return getCognitiveOuterSourceIndex(ModIndex.getMod6Index1(beltIndex - 1) - (ModIndex.getMod6Index1(beltIndex) - spaceIndex - 1));
        } else if(spaceIndex < ModIndex.getMod6Index2(beltIndex)) {
            return lastIndex - 6;
        } else if(spaceIndex < ModIndex.getMod6Index3(beltIndex)) {
            return lastIndex - 7;
        } else if(spaceIndex < ModIndex.getMod6Index4(beltIndex)) {
            return spaceIndex == ModIndex.getMod6Index3(beltIndex) ? lastIndex - 8 :
                    getCognitiveOuterDeciderIndex(spaceIndex - 1);
        } else if(spaceIndex < ModIndex.getMod6Index5(beltIndex)) {
            return spaceIndex == ModIndex.getMod6Index4(beltIndex) ? lastIndex - 15 :
                    getCognitiveOuterSourceIndex(spaceIndex - 1);
        } else if(spaceIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return spaceIndex == ModIndex.getMod6Index5(beltIndex) ? getCognitiveOuterDeciderIndex(ModIndex.getMod6Index5(beltIndex-1)) :
                    getCognitiveOuterSourceIndex(ModIndex.getMod6Index5(beltIndex - 1) + (spaceIndex - ModIndex.getMod6Index5(beltIndex) - 1));
        }
        return spaceIndex == ModIndex.getMod6Index5(beltIndex) ? getCognitiveOuterDeciderIndex(ModIndex.getMod6Index5(beltIndex-1)) :
                getCognitiveOuterSourceIndex(ModIndex.getMod6Index5(beltIndex - 1) + (spaceIndex - ModIndex.getMod6Index5(beltIndex) - 1));
    }

    default int getCognitiveOuterDeciderIndex(int spaceIndex) {
        int beltIndex = getBeltIndex(spaceIndex);
        int lastIndex = getLastIndex(spaceIndex);
        if(spaceIndex == 0) {
            return 5;
        }
        if(spaceIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return  spaceIndex - 1 == ModIndex.getMod6Index0(beltIndex - 1) ? lastIndex - 15 :
                    getCognitiveOuterSourceIndex(ModIndex.getMod6Index1(beltIndex - 1) - (ModIndex.getMod6Index1(beltIndex) - spaceIndex));
        } else if(spaceIndex < ModIndex.getMod6Index2(beltIndex)) {
            return spaceIndex == ModIndex.getMod6Index1(beltIndex) ? getCognitiveOuterSourceIndex(ModIndex.getMod6Index1(beltIndex-1)) :
                    getCognitiveOuterSensorIndex(ModIndex.getMod6Index1(beltIndex-1) + (spaceIndex - ModIndex.getMod6Index1(beltIndex) - 1));
        } else if(spaceIndex < ModIndex.getMod6Index3(beltIndex)) {
            return spaceIndex == ModIndex.getMod6Index2(beltIndex) ? getCognitiveOuterSensorIndex(ModIndex.getMod6Index2(beltIndex-1)) :
                    getCognitiveOuterSensorIndex(ModIndex.getMod6Index3(beltIndex-1) - (ModIndex.getMod6Index3(beltIndex) - spaceIndex - 1));
        } else if(spaceIndex < ModIndex.getMod6Index4(beltIndex)) {
            return lastIndex - 6;
        } else if(spaceIndex < ModIndex.getMod6Index5(beltIndex)) {
            return lastIndex - 7;
        } else if(spaceIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return spaceIndex == ModIndex.getMod6Index5(beltIndex) ? lastIndex - 8 :
                    getCognitiveOuterSourceIndex(spaceIndex - 1);
        }
        return spaceIndex == ModIndex.getMod6Index5(beltIndex) ? lastIndex - 8 :
                getCognitiveOuterSourceIndex(spaceIndex - 1);
    }

    default int getSocialOuterSourceIndex(int spaceIndex) {
        int beltIndex = getBeltIndex(spaceIndex);
        int lastIndex = getLastIndex(spaceIndex);
        if(spaceIndex == 0) {
            return 4;
        }
        if(spaceIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return  spaceIndex - 1 == ModIndex.getMod6Index0(beltIndex - 1) ? getSocialOuterSensorIndex(ModIndex.getMod6Index1(beltIndex-1) - (ModIndex.getMod6Index1(beltIndex) - spaceIndex - 1)) :
                    getSocialOuterDeciderIndex(ModIndex.getMod6Index1(beltIndex - 1) - (ModIndex.getMod6Index1(beltIndex) - spaceIndex));
        } else if(spaceIndex < ModIndex.getMod6Index2(beltIndex)) {
            return getSocialOuterDeciderIndex(ModIndex.getMod6Index1(beltIndex-1) + (spaceIndex - ModIndex.getMod6Index1(beltIndex)));
        } else if(spaceIndex < ModIndex.getMod6Index3(beltIndex)) {
            return lastIndex - 6;
        } else if(spaceIndex < ModIndex.getMod6Index4(beltIndex)) {
            return lastIndex - 7;
        } else if(spaceIndex < ModIndex.getMod6Index5(beltIndex)) {
            return spaceIndex == ModIndex.getMod6Index4(beltIndex) ? lastIndex - 8 :
                    getSocialOuterSensorIndex(spaceIndex - 1);
        } else if(spaceIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return  spaceIndex == ModIndex.getMod6Index5(beltIndex) ? getSocialOuterSensorIndex(spaceIndex - 1) :
                    getSocialOuterSensorIndex(ModIndex.getMod6Index5(beltIndex-1) + (spaceIndex - ModIndex.getMod6Index5(beltIndex) - 1));
        }
        return  spaceIndex == ModIndex.getMod6Index5(beltIndex) ? getSocialOuterSensorIndex(spaceIndex - 1) :
                getSocialOuterSensorIndex(ModIndex.getMod6Index5(beltIndex-1) + (spaceIndex - ModIndex.getMod6Index5(beltIndex) - 1));
    }

    default int getSocialOuterSensorIndex(int spaceIndex) {
        int beltIndex = getBeltIndex(spaceIndex);
        int lastIndex = getLastIndex(spaceIndex);
        if(spaceIndex == 0) {
            return 6;
        }
        if(spaceIndex == 1) {
            return 13;
        }
        if(spaceIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return spaceIndex - 1 == ModIndex.getMod6Index0(beltIndex - 1) ? lastIndex - 8 :
                    getSocialOuterDeciderIndex(spaceIndex - 1);
        } else if(spaceIndex < ModIndex.getMod6Index2(beltIndex)) {
            return spaceIndex == ModIndex.getMod6Index1(beltIndex) ? getSocialOuterDeciderIndex(spaceIndex - 1) :
                    getSocialOuterDeciderIndex(ModIndex.getMod6Index1(beltIndex-1) + (spaceIndex - ModIndex.getMod6Index1(beltIndex) - 1));
        } else if(spaceIndex < ModIndex.getMod6Index3(beltIndex)) {
            return spaceIndex == ModIndex.getMod6Index2(beltIndex) ? getSocialOuterDeciderIndex(ModIndex.getMod6Index2(beltIndex - 1)) :
                    getSocialOuterSourceIndex(ModIndex.getMod6Index2(beltIndex-1) + (spaceIndex - ModIndex.getMod6Index2(beltIndex) - 1));
        } else if(spaceIndex < ModIndex.getMod6Index4(beltIndex)) {
            return spaceIndex == ModIndex.getMod6Index3(beltIndex) ? getSocialOuterSourceIndex(ModIndex.getMod6Index3(beltIndex-1)) :
                    getSocialOuterSourceIndex(ModIndex.getMod6Index4(beltIndex - 1) - (ModIndex.getMod6Index4(beltIndex) - spaceIndex - 1));
        } else if(spaceIndex < ModIndex.getMod6Index5(beltIndex)) {
            return lastIndex - 6;
        } else if(spaceIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return lastIndex - 7;
        }
        return lastIndex - 7;
    }

    default int getSocialOuterDeciderIndex(int spaceIndex) {
        int beltIndex = getBeltIndex(spaceIndex);
        int lastIndex = getLastIndex(spaceIndex);
        if(spaceIndex == 0) {
            return 2;
        }
        if(spaceIndex == 1) {
            return 15;
        }
        if(spaceIndex  < ModIndex.getMod6Index1(beltIndex)) {
            return lastIndex - 6;
        } else if(spaceIndex < ModIndex.getMod6Index2(beltIndex)) {
            return lastIndex - 7;
        } else if(spaceIndex < ModIndex.getMod6Index3(beltIndex)) {
            return spaceIndex == ModIndex.getMod6Index2(beltIndex) ? lastIndex - 8 :
                    getSocialOuterSourceIndex(spaceIndex - 1);
        } else if(spaceIndex < ModIndex.getMod6Index4(beltIndex)) {
            return spaceIndex == ModIndex.getMod6Index3(beltIndex) ? getSocialOuterSourceIndex(spaceIndex-1) :
                    getSocialOuterSourceIndex(ModIndex.getMod6Index3(beltIndex - 1) + (spaceIndex - ModIndex.getMod6Index3(beltIndex) - 1));
        } else if(spaceIndex < ModIndex.getMod6Index5(beltIndex)) {
            return spaceIndex == ModIndex.getMod6Index4(beltIndex) ? getSocialOuterSensorIndex(spaceIndex - 1) :
                    getSocialOuterSensorIndex(ModIndex.getMod6Index4(beltIndex - 1) + (spaceIndex - ModIndex.getMod6Index4(beltIndex) - 1));
        } else if(spaceIndex < ModIndex.getMod6Index0(beltIndex + 1)) {
            return getSocialOuterSensorIndex(ModIndex.getMod6Index5(beltIndex - 1) + (spaceIndex - ModIndex.getMod6Index5(beltIndex)));
        }
        return getSocialOuterSensorIndex(ModIndex.getMod6Index5(beltIndex - 1) + (spaceIndex - ModIndex.getMod6Index5(beltIndex)));
    }

}
