SUMMARY = "LDD3 misc-modules examples"
HOMEPAGE = "https://github.com/<you-or-org>/ldd3"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit module update-rc.d

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignment-7-ryed5569.git;protocol=ssh;branch=master \
           file://0001-LDD3-build-only-scull-and-misc-modules-include-path-.patch \
           file://misc-modules-start-stop \
           "

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "2e16081e6cfd66d92f09f208648ad732b81dee2b"

S = "${WORKDIR}/git"

# Build only the misc-modules subtree
EXTRA_OEMAKE = " -C ${STAGING_KERNEL_DIR} M=${S}/misc-modules \
                 EXTRA_CFLAGS='-I${S}/include -I${S}/misc-modules'"
#MODULES_INSTALL_TARGET = "all"

#MODULES_INSTALL_TARGET = "modules_install"
INITSCRIPT_PACKAGES = "${PN}"
INSTALL_MOD_DIR = "extra"
INITSCRIPT_NAME = "misc-modules-start-stop"
INITSCRIPT_PARAMS = "defaults 98"

FILES:${PN} += "${libdir}/modules/${KERNEL_VERSION}/extra/faulty.ko"
FILES:${PN} += "${libdir}/modules/${KERNEL_VERSION}/extra/hello.ko"
FILES:${PN} += "${sysconfdir}/init.d/misc-modules-start-stop"

RPROVIDES:${PN} += "kernel-module-faulty"

do_install:append() {
    install -d ${D}${libdir}/modules/${KERNEL_VERSION}/extra/
	install -m 0755 ${S}/misc-modules/faulty.ko ${D}${libdir}/modules/${KERNEL_VERSION}/extra/
	install -m 0755 ${S}/misc-modules/hello.ko ${D}${libdir}/modules/${KERNEL_VERSION}/extra/
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/misc-modules-start-stop ${D}${sysconfdir}/init.d/
}
