/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.ml.selection.score;

/**
 * Pair of truth value and predicated by model.
 *
 * @param <L> Type of a label (truth or prediction).
 */
public class TruthWithPrediction<L> {
    /** Truth value. */
    private final L truth;

    /** Predicted value. */
    private final L prediction;

    /**
     * Constructs a new instance of truth with prediction.
     *
     * @param truth Truth value.
     * @param prediction Predicted value.
     */
    public TruthWithPrediction(L truth, L prediction) {
        this.truth = truth;
        this.prediction = prediction;
    }

    /** */
    public L getTruth() {
        return truth;
    }

    /** */
    public L getPrediction() {
        return prediction;
    }
}
