# Waqt 3.0 — daily Muslim companion

Static, mobile-first Progressive Web App. No build step or account. Existing Blutobox is not modified.

## Publish
1. The Waqt source is hosted in `azrxc/Q`; deploy its main branch.
2. Use Settings → Pages → Deploy from a branch → main / (root), or import the repository into Vercel with framework preset Other and no build command. Enable HTTPS. The app uses relative asset URLs and can be served under a project path.
3. Open the deployed URL on each family member’s device. Android: Chrome → menu → Install app / Add to Home Screen. iPhone: Safari → Share → Add to Home Screen. On first launch choose the correct city or GPS location, calculation method and Asr school, and compare prayer times with the official local timetable.

## What is included
- Calculated local prayer times, next-prayer countdown, Hijri date, Qibla bearing and optional compass, private prayer journal, optional in-app chime and user-supplied Azan audio, digital tasbih.
- Quran reader for all 114 surahs via Al Quran Cloud, Arabic Uthmani text, Malay Basmeih and English Saheeh International translations, per-ayah Alafasy recitation, local bookmarks, last-read position, configurable Arabic font size, cached most recent surah.
- Twelve offline Quranic dua excerpts, English and Malay explanatory meanings, search, filters and saved favourites. Verse references open Quran.com. Further collection links to JAKIM and Hisn al-Muslim.
- Mosque discovery via GPS and OpenStreetMap Overpass (5 km search, straight-line distance); Maps search by town or nearby; official Malaysian mosque-directory links.
- Community gateway to JAKIM, IKIM and Islamic Relief Malaysia updates, Islamic learning, hadith and religious calendar. No aggregated live headline feed.
- Giving directory links to official Islamic Relief Malaysia, PPZ-MAIWP and government state zakat authorities. No funds are accepted, processed or tracked by Waqt.
- Optional private daily practice and fasting journal, large text, automatic dark mode, share link and installable offline shell. No cloud sync, sign-in, analytics or ads. Each person's journal remains on their own browser/device.

## Before public release
Prayer times use AlAdhan calculation, not authoritative JAKIM zone-specific times. Confirm all religious text, translations, rights, local calculation methods, timezone and daylight saving behaviour with qualified reviewers and official sources. Live API behaviour and CORS are not tested. Quran and mosque lookups can be unavailable or rate-limited; fallbacks link to external resources. Location is shared with AlAdhan or Overpass only as required by a chosen feature; external links have their own privacy terms. Mosque results are community-mapped and may be incomplete. Hijri dates can differ by local moon sighting. Background Azan playback is not guaranteed; native notifications need a separate native app implementation and permissions. The project name is provisional; check trademark and domain availability before launch. Confirm rights to translations and recordings before commercial use.

## Test status
JavaScript syntax passed. Browser document tests with mocked API fixtures (not live religious data) passed for navigation, Quran selection/bookmarks, dua search/save, mosque lookup, local journal and fasting persistence, text size, prayer display and layout without horizontal overflow at 320/390/768/1280 px. Direct browser navigation was blocked in this environment, so hosted PWA install/offline behaviour and real-device audio/GPS remain unverified. The standalone preview is for inspection only; the hosted repository contains the installable PWA.
