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

package org.apache.ignite.internal.processors.cache.distributed.dht.preloader;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.apache.ignite.internal.util.typedef.T2;
import org.apache.ignite.internal.util.typedef.internal.U;

/**
 *
 */
public class CachePartitionPartialCountersMap {
    /** */
    public static final CachePartitionPartialCountersMap EMPTY = new CachePartitionPartialCountersMap();

    /** */
    private int[] partIds;

    /** */
    private long[] initialUpdCntrs;

    /** */
    private long[] updCntrs;

    /** */
    private int curIdx;

    /** */
    private CachePartitionPartialCountersMap() {
        // Empty map.
    }

    /**
     * @param partsCnt Total number of partitions will be stored in the partial map.
     */
    public CachePartitionPartialCountersMap(int partsCnt) {
        partIds = new int[partsCnt];
        initialUpdCntrs = new long[partsCnt];
        updCntrs = new long[partsCnt];
    }

    /**
     * @return Total number of partitions added to the map.
     */
    public int size() {
        return curIdx;
    }

    /**
     * Adds partition counters for a partition with the given ID.
     *
     * @param partId Partition ID to add.
     * @param initialUpdCntr Partition initial update counter.
     * @param updCntr Partition update counter.
     */
    public void add(int partId, long initialUpdCntr, long updCntr) {
        if (curIdx > 0) {
            if (partIds[curIdx - 1] >= partId)
                throw new IllegalArgumentException("Adding a partition in the wrong order " +
                    "[prevPart=" + partIds[curIdx - 1] + ", partId=" + partId + ']');
        }

        if (curIdx == partIds.length)
            throw new IllegalStateException("Adding more partitions than reserved: " + partIds.length);

        partIds[curIdx] = partId;
        initialUpdCntrs[curIdx] = initialUpdCntr;
        updCntrs[curIdx] = updCntr;

        curIdx++;
    }

    /**
     * @param partId Partition ID to search.
     * @return Partition index in the array.
     */
    public int partitionIndex(int partId) {
        return Arrays.binarySearch(partIds, 0, curIdx, partId);
    }

    /**
     * Gets partition ID saved at the given index.
     *
     * @param idx Index to get value from.
     * @return Partition ID.
     */
    public int partitionAt(int idx) {
        return partIds[idx];
    }

    /**
     * Gets initial update counter saved at the given index.
     *
     * @param idx Index to get value from.
     * @return Initial update counter.
     */
    public long initialUpdateCounterAt(int idx) {
        return initialUpdCntrs[idx];
    }

    /**
     * Gets update counter saved at the given index.
     *
     * @param idx Index to get value from.
     * @return Update counter.
     */
    public long updateCounterAt(int idx) {
        return updCntrs[idx];
    }


    /**
     * @param cntrsMap Partial local counters map.
     * @return Partition ID to partition counters map.
     */
    public static Map<Integer, T2<Long, Long>> toCountersMap(CachePartitionPartialCountersMap cntrsMap) {
        if (cntrsMap.size() == 0)
            return Collections.emptyMap();

        Map<Integer, T2<Long, Long>> res = U.newHashMap(cntrsMap.size());

        for (int idx = 0; idx < cntrsMap.size(); idx++)
            res.put(cntrsMap.partitionAt(idx),
                new T2<>(cntrsMap.initialUpdateCounterAt(idx), cntrsMap.updateCounterAt(idx)));

        return res;
    }
}