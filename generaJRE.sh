MODS=$(jdeps --module-path "D:/Java/jdk-17/jmods;D:/workspace/modulosBaxen" --list-deps --multi-release 17 --ignore-missing-deps D:/workspace/proyecto-baxen-terragene/baxen/target/baxen-0.0.1-SNAPSHOT.jar | tr '\n' ',')
echo ${MODS%,}

jlink --module-path "D:/Java/jdk-17/jmods;D:/workspace/modulosBaxen" --add-modules java.base --output D:/workspace/jre