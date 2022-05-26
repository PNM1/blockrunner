package com.game;

public interface FireBaseInterface {
    
    public void SetOnValueChangedListener(DataHolderClass dataholder);

    public void SetValueInDb(String target, String value);

}
