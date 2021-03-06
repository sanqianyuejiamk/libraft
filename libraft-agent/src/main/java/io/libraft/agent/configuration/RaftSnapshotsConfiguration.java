/*
 * Copyright (c) 2013 - 2014, Allen A. George <allen dot george at gmail dot com>
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of libraft nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.libraft.agent.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import io.libraft.algorithm.RaftConstants;
import org.hibernate.validator.constraints.NotEmpty;

import javax.annotation.Nullable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Represents the snapshot configuration. Contains the following properties/blocks:
 * <ul>
 *     <li>minEntriesToSnapshot.</li>
 *     <li>snapshotCheckInterval.</li>
 *     <li>snapshotsDirectory</li>
 * </ul>
 * See the project README.md for more on the configuration.
 */
public final class RaftSnapshotsConfiguration {

    private static final String MIN_ENTRIES_TO_SNAPSHOT = "minEntriesToSnapshot";
    private static final String SNAPSHOT_CHECK_INTERVAL = "snapshotCheckInterval";
    private static final String SNAPSHOTS_DIRECTORY = "snapshotsDirectory";

    @NotNull
    @MinEntriesToSnapshot(ceil = Integer.MAX_VALUE - 1) // I can't say "infinite", so I'll use this instead
    @JsonProperty(MIN_ENTRIES_TO_SNAPSHOT)
    private int minEntriesToSnapshot = RaftConstants.SNAPSHOTS_DISABLED;

    @Min(RaftConfigurationConstants.ONE_SECOND)
    @Max(RaftConfigurationConstants.TWELVE_HOURS)
    @JsonProperty(SNAPSHOT_CHECK_INTERVAL)
    private long snapshotCheckInterval = RaftConstants.SNAPSHOT_CHECK_INTERVAL;

    @NotEmpty
    @JsonProperty(SNAPSHOTS_DIRECTORY)
    private String snapshotsDirectory = RaftConfigurationConstants.DEFAULT_SNAPSHOTS_DIRECTORY;

    /**
     * Get the minimum number of committed entries that have
     * to be present in the Raft log for a snapshot to be made.
     * By default snapshots are <strong>disabled</strong>.
     *
     * @return either {@link RaftConstants#SNAPSHOTS_DISABLED}, or
     * a value in the range (1 <= minEntriesToSnapshot <= MAX_INT - 1)
     */
    public int getMinEntriesToSnapshot() {
        return minEntriesToSnapshot;
    }

    /**
     * Set the minimum number of committed entries that have
     * to be present in the Raft log for a snapshot to be made.
     *
     * @param minEntriesToSnapshot minimum number of committed entries
     *                             that have to be present in the Raft log for
     *                             a snapshot to be made. Valid values are either
     *                             {@link RaftConstants#SNAPSHOTS_DISABLED}, or
     *                             a value in the range (1 <= minEntriesToSnapshot <= MAX_INT - 1)
     */
    public void setMinEntriesToSnapshot(int minEntriesToSnapshot) {
        this.minEntriesToSnapshot = minEntriesToSnapshot;
    }

    /**
     * Get the interval at which to check whether a snapshot should be made.
     * Defaults to {@link RaftConstants#SNAPSHOT_CHECK_INTERVAL}.
     *
     * @return interval in milliseconds in the range
     * ({@link RaftConfigurationConstants#ONE_SECOND} <= interval <= {@link RaftConfigurationConstants#TWELVE_HOURS})
     * at which to which to check whether a snapshot should be made
     */
    public long getSnapshotCheckInterval() {
        return snapshotCheckInterval;
    }

    /**
     * Set the interval at which to check whether a snapshot should be made.
     *
     * @param snapshotCheckInterval interval in milliseconds in the range
     * ({@link RaftConfigurationConstants#ONE_SECOND} <= interval <= {@link RaftConfigurationConstants#TWELVE_HOURS})
     * at which to check whether a snapshot should be made
     */
    public void setSnapshotCheckInterval(long snapshotCheckInterval) {
        this.snapshotCheckInterval = snapshotCheckInterval;
    }

    /**
     * Get the directory in which snapshot files are stored.
     * Defaults to {@link RaftConfigurationConstants#DEFAULT_SNAPSHOTS_DIRECTORY}.
     *
     * @return non-null, non-empty directory in which snapshot files are stored
     */
    public String getSnapshotsDirectory() {
        return snapshotsDirectory;
    }

    /**
     * Set the directory in which snapshot files are stored.
     *
     * @param snapshotsDirectory non-null, non-empty directory in which snapshot files are stored.
     *                           This can be either an absolute or relative (in relation to {@code cwd}) path
     */
    public void setSnapshotsDirectory(String snapshotsDirectory) {
        this.snapshotsDirectory = snapshotsDirectory;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RaftSnapshotsConfiguration other = (RaftSnapshotsConfiguration) o;
        return minEntriesToSnapshot == other.minEntriesToSnapshot
                && snapshotCheckInterval == other.snapshotCheckInterval
                && snapshotsDirectory.equals(other.snapshotsDirectory);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(minEntriesToSnapshot, snapshotCheckInterval, snapshotsDirectory);
    }

    @Override
    public String toString() {
        return Objects
                .toStringHelper(this)
                .add(MIN_ENTRIES_TO_SNAPSHOT, minEntriesToSnapshot)
                .add(SNAPSHOT_CHECK_INTERVAL, snapshotCheckInterval)
                .add(SNAPSHOTS_DIRECTORY, snapshotsDirectory)
                .toString();
    }
}
