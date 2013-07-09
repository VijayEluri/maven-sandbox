Todo
====

### Source Release

* Option to fail the build (or separate goal), so we don't have to check the report daily, but let the CI server mail when failing.
    * notice that failing the build will make the report unusable: so we must have either a separate goal or a mail sent explicitely from plugin
    * there are some known issues with old artifacts, which don't have newer version expected: need to configure these as "accepted exceptions"


### Sites

* Check if artifact versions are up to date in listing pages:
    * http://maven.apache.org/plugins/
    * http://maven.apache.org/shared/
    * http://maven.apache.org/pom/
    * http://maven.apache.org/skins/

* Option to fail the build (or separate goal), so we don't have to check the report daily, but let the CI server mail when failing

# Done

* take screenshot of site. ( os specific )

* See where to publish the result on Maven site
    * jenkins site URL https://builds.apache.org/view/M-R/view/Maven/job/dist-tool-plugin/site

### Source Release
* add date of release (from artifact)
  * date from metadata is ok ?

* Check older artifact in dist (only latest should be present, previous version removed)

* Shell command for cleaning older artifacts

### Sites

* add date of site
    * [sky] date from comment part in skin (no always ok :( )
* remove "check if version present", since our inheritance ensures we don't have problems
    * [sky] kept only for precise layout check
* replace Skins columns with one unique column "Skin used", showing which skin is used (with version)
    * [sky] no garantee some site like http://maven.apache.org/plugins/maven-one-plugin/ have no style header 
* add date of release (from artifact)
    * [sky] date from metadata is ok ?