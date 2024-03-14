## 8.0.2
* Changed the `getTotalCookTime` overwrite to be a head-inject-cancel, as the overwrite was failing when an AT was present.

## 8.0.1
* Removed forge dependency line from the mods.toml and marked as Forge and NeoForge for CF.
  * The dependency will be added back and the Forge marker will be removed once CF supports Neo correctly.

## 8.0.0
* Updated to 1.20.1

## 7.0.0
* Updated to 1.19.2

## 6.0.3
* Rebuilt for 1.18.2

## 6.0.2
* Update to Placebo 6.1.0

## 6.0.1
* Changed the signature of the getTotalCookTime overwrite to public to not crash when AT's are involved.

## 6.0.0
* Updated to 1.18.1

## 5.0.0
* Updated to 1.17.1

## 4.5.0
* FastFurnace has been rewritten to use Mixins!
* FastFurnace is no longer required on the client side - it only needs to be present on the server.