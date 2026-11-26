# Waqt competitor research — 26 November 2026

Scope: Malaysia-first prayer utilities, selected international apps, published user reviews, Android documentation. Not an exhaustive world market survey.

- SolatTime: https://solattime.mysolat.my/en/ — official timetable, GPS, system alarms, widgets.
- Waktu Solat Malaysia: https://play.google.com/store/apps/details?id=com.murad.waktusolat — reviews mention widget problems.
- Waktu Solat Malaysia – Azan reviews: https://apps.apple.com/my/app/waktu-solat-malaysia-azan/id1443457663?platform=iphone&see-all=reviews — sound choices, early preparation alerts, automatic location problems.
- Pillars: https://thepillarsapp.com/prayer-notifications and https://thepillarsapp.com/privacy — per-prayer sound choices, early reminders, privacy.
- Android exact alarms: https://developer.android.com/develop/background-work/services/alarms — exact alarm permission and resource trade-offs.
- GPS API: https://api.waktusolat.app/docs — documented /v2/solat/gps/{lat}/{long}; do not guess from nearest city.

Implemented in 0.4: per-prayer full azan / silent notification / off; optional 5/10/15/30-minute silent preparation alerts; native next-event scheduling; diagnostics for exact alarms, notifications, alarm volume, power saver and timetable coverage; JS syntax validation in CI. Web remains foreground-only, with early reminders shown in the status area. Android notifications require permission and phone policies can suppress them. GPS still relies on a provider and cannot guarantee accuracy near zone boundaries. Next priorities: live timetable/zone comparison, on-device locked-screen test, offline zone polygons with validated licensing and special-zone coverage, native widgets, larger-text onboarding, update signing. No ads/accounts required for core worship.
