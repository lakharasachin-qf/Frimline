package com.app.cartcounter.strategy;

import com.app.cartcounter.PreviousProgress

/**
 * Created by 张宇 on 2018/3/6.
 * E-mail: zhangyu4@yy.com
 * YY: 909017428
 */
class StickyStrategy(val factor: Double) : NormalAnimationStrategy() {

    init {
        if (factor <= 0.0 && factor > 1.0) {
            throw IllegalStateException("factor must be in range (0,1] but now is $factor")
        }
    }

    override fun getFactor(previousProgress: PreviousProgress, index: Int, size: Int, charList: List<Char>): Double {
        return factor
    }
}