package com.github.miwu.ui.miot

import android.app.Activity
import com.github.miwu.logic.network.MiotSpecService
import com.github.miwu.startActivity
import com.github.miwu.ui.DeviceActivity
import com.github.miwu.ui.miot.device.EmptyFragment
import com.github.miwu.ui.miot.device.LightDefaultFragment
import com.github.miwu.ui.miot.device.OutletDefaultFragment
import com.github.miwu.ui.miot.device.THSensorDefaultFragment
import kotlin.concurrent.thread

object DeviceUtils {

    fun Activity.startDeviceActivity(model: String, did: String, name: String, specType: String) {
        this.startActivity<DeviceActivity> {
            putExtra("model", model)
            putExtra("did", did)
            putExtra("name", name)
            putExtra("specType", specType)
        }
    }

    fun getDeviceFragment(model: String, specType: String, block: (BaseFragment) -> Unit) {
        when (model) {

            else -> {
                thread {
                    val miotDevice =
                        MiotSpecService.getInstanceServices(specType)
                            ?: return@thread block(
                                EmptyFragment()
                            )
                    val urn = MiotSpecService.parseUrn(miotDevice.type) ?: return@thread block(
                        EmptyFragment()
                    )
                    block(
                        when (urn.value) {
                            "light" -> {
                                LightDefaultFragment(miotDevice.services)
                            }

                            "temperature-humidity-sensor" -> {
                                THSensorDefaultFragment(miotDevice.services)
                            }

                            "outlet" -> {
                                OutletDefaultFragment(miotDevice.services)
                            }

                            else -> {
                                EmptyFragment()
                            }
                        }
                    )
                }
            }
        }
    }


}