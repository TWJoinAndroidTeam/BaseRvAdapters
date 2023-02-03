package com.example.baseadapterslibrary.module

public sealed class NormalRvLoadState {

    class NoData(val isFinishAdd: Boolean) : NormalRvLoadState()

    object HaveData : NormalRvLoadState()

}
