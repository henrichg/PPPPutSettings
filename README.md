<!--suppress CheckImageSize -->
<img src="art/ic_launcher-web.png"  alt="PPPPS application icon" width="100" height="100">  

[![Stand With Ukraine](https://raw.githubusercontent.com/vshymanskyy/StandWithUkraine/main/badges/StandWithUkraine.svg)](https://stand-with-ukraine.pp.ua)

PPPPutSettings (PPPPS)
====================================

[![version](https://img.shields.io/badge/version-2.0-blue)](https://github.com/henrichg/PPPPutSettings/releases/tag/2.0)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![License](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/henrichg/PPPPutSettings/blob/master/LICENSE)
[![Crowdin](https://badges.crowdin.net/phoneprofilesplus/localized.svg)](https://crowdin.com/project/phoneprofilesplus)\
\
[![GMail](https://img.shields.io/badge/Gmail-D14836?logo=gmail&logoColor=white&label=henrich.gron@gmail.com)](mailto:henrich.gron@gmail.com)
[![Discord](https://img.shields.io/badge/Discord-5865F2?logo=discord&logoColor=white&label=PPP%20server)](https://discord.com/channels/1258733423426670633/1259190332206219314)
[![XDA-developers](https://img.shields.io/badge/xda%20developers-2DAAE9?logo=xda-developers&logoColor=white&label=PhoneProfilesPlus)](https://xdaforums.com/t/app-phoneprofilesplus.3799429/)
[![Reddit](https://img.shields.io/badge/Reddit-FF4500?logo=reddit&logoColor=white&label=u/henrichg)](https://www.reddit.com/user/henrichg/)
[![Bluesky](https://img.shields.io/badge/Bluesky-0285FF?logo=bluesky&logoColor=fff&label=@henrichg)](https://bsky.app/profile/henrichg.bsky.social)\
Discord PPP server invitation: https://discord.gg/Yb5hgAstQ3

[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=AF5QK49DMAL2U&currency_code=EUR)

__Install PPPPutSettings directly from PhoneProfilesPlus. But it is also possible to install it from an external source. If installing from PhoneProfilesPlus does not work, use external source. External sources are described below.__\
__There is an exception. For Android 14+, PPPPutSettigs can only be installed in a special way. Read [Installation for Android 14+](docs/install_apk_in_device.md)__

### Sources of PPPPutSettings:

Use keyword "PPPPutSettings" for search this application in these stores:

__Droid-ify (F-Droid alternative):__
- [PPPPS release](https://apt.izzysoft.de/fdroid/index/apk/sk.henrichg.pppputsettings)
- [Store applicaion (download)](https://apt.izzysoft.de/fdroid/index/apk/com.looker.droidify)
  &nbsp;&nbsp;&nbsp;_IzzyOnDroid repository is included_

__Neo Store (F-Droid alternative, preferred over GitHub):__
- [PPPPS release](https://apt.izzysoft.de/fdroid/index/apk/sk.henrichg.pppputsettings)
- [Store applicaion (download)](https://apt.izzysoft.de/fdroid/index/apk/com.machiav3lli.fdroid)
  &nbsp;&nbsp;&nbsp;_IzzyOnDroid repository is included_

__F-Droid:__
- [PPPPS release](https://apt.izzysoft.de/fdroid/index/apk/sk.henrichg.pppputsettings)
  &nbsp;&nbsp;&nbsp;_[How to add IzzyOnDroid repository to F-Droid application](https://apt.izzysoft.de/fdroid/index/info)_
- [Store application (download)](https://www.f-droid.org/)

When the dialog "Unsafe app blocked" from Google Play Protect appears during installation, click at bottom, text "More details" and in it at bottom, text "Install anyway".

__GitHub:__
- [PPPPS release (direct download)](https://github.com/henrichg/PPPPutSettings/releases/latest/download/PPPPutSettings.apk)
&nbsp;&nbsp;&nbsp;_[Number of downloads by version](https://hanadigital.github.io/grev/?user=henrichg&repo=pppputsettings)_

__How to install PPPPutSettings (for Android 14+)__
- [How to install](docs/install_apk_in_device.md)

If is not possible to install PPPPutSettings from the downloaded apk file directly on your device, you can install it from your computer.
- [How to install](docs/install_apk_from_pc.md)

---
__What is PPPPutSettings:__

Android application for put settings parameter to system database without root. It is helper application for [PhoneProfilesPlus](https://github.com/henrichg/PhoneProfilesPlus).

- [Privacy Policy](https://henrichg.github.io/PhoneProfilesPlus/privacy_policy.html)

_**** Please report me bugs, comments and suggestions to my e-mail: <henrich.gron@gmail.com>. Speed up the especially bug fixes. Thank you very much. ****_

_*** Please help me with translation, thank you: <https://crowdin.com/project/phoneprofilesplus> ***_

##### Permissions
- __[Show it](docs/permissions.md)__

##### Screenshots
- [[1]](art/phoneScreenshots/01.png),
[[2]](art/phoneScreenshots/02.png),

##### Supported Android versions

- From Android 5.1
- minSdkVersion = 22
- targetSdkVersion = 22
- compiledSdkVersion = 35

##### Required external libs - open-source

- AndroidX library: appcompat, splashscreen - https://developer.android.com/jetpack/androidx/versions
- ACRA - https://github.com/ACRA/acra
- guava - https://github.com/google/guava
- AutoService = https://github.com/google/auto/tree/main/service
- Multi-language_App (only modified class LocaleHelper.java) https://github.com/anurajr1/Multi-language_App
