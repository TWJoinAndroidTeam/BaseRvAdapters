package com.example.baseadapterslibrary.model

public sealed class NormalRvLoadState {

    class NoData(val isFinishAdd: Boolean) : NormalRvLoadState()

    object HaveData : NormalRvLoadState()

}
