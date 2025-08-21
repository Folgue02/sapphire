package io.github.folgue02.sapphire.filter;

/// Represents the different priorities for a filter, can be
/// used to determine which filters have to be executed first.
///
/// **NOTE:** [EXTREME] is higher than [HIGH], therefore, **filters with
/// higher priority will be executed first**.
public enum FilterPriority {
	EXTREME,
	HIGH,
	DEFAULT,
	LOW,
}