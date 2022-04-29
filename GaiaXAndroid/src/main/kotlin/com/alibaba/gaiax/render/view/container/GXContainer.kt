/*
 * Copyright (c) 2021, Alibaba Group Holding Limited;
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.gaiax.render.view.container

import android.content.Context
import android.graphics.Outline
import android.graphics.drawable.GradientDrawable
import android.support.annotation.Keep
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import com.alibaba.fastjson.JSONObject
import com.alibaba.gaiax.context.GXTemplateContext
import com.alibaba.gaiax.render.view.GXIRootView
import com.alibaba.gaiax.render.view.GXIRoundCorner
import com.alibaba.gaiax.render.view.GXIViewBindData


/**
 * @suppress
 */
@Keep
open class GXContainer : RecyclerView, GXIViewBindData, GXIRootView, GXIRoundCorner {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private fun finalize() {
        if (gxTemplateContext?.rootView?.get() == this) {
            if (gxTemplateContext?.rootNode?.isRoot == true) {
                gxTemplateContext?.release()
                gxTemplateContext = null
            }
        }
    }

    override fun onBindData(data: JSONObject) {
    }

    private var gxTemplateContext: GXTemplateContext? = null

    override fun setTemplateContext(gxContext: GXTemplateContext) {
        this.gxTemplateContext = gxContext
    }

    override fun getTemplateContext(): GXTemplateContext? {
        return gxTemplateContext
    }

    override fun setRoundCornerRadius(radius: FloatArray) {
        if (radius.size == 8) {
            val tl = radius[0]
            val tr = radius[2]
            val bl = radius[4]
            val br = radius[6]
            if (tl == tr && tr == bl && bl == br) {
                this.clipToOutline = true
                this.outlineProvider = object : ViewOutlineProvider() {
                    override fun getOutline(view: View, outline: Outline) {
                        if (alpha >= 0.0f) {
                            outline.alpha = alpha
                        }
                        outline.setRoundRect(0, 0, view.width, view.height, tl)
                    }
                }
            } else {
                val shape = GradientDrawable()
                shape.shape = GradientDrawable.RECTANGLE
                shape.cornerRadii = radius
                background = shape
            }
        }
    }

    override fun setRoundCornerBorder(borderColor: Int, borderWidth: Float, radius: FloatArray) {
        if (radius.size == 8) {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadii = radius
            if (borderWidth.toInt() > 0) {
                shape.setStroke(borderWidth.toInt(), borderColor)
            }
            background = shape
        }
    }
}