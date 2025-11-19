# Legacy Database

## How to get usermod UUID

Deploy the legacydb on tools namespace. Once is running, it will probably go into a CrashLoopBackOff state because the wrong user id that has no permission to run on openshift.

To get the userID that OpenShift assigned to the pod, run the following command:

```bash
oc get pod <POD ID> -n <NAMESPACE>-tools -o json
```

Look for the `securityContext` section in the output, and find the `runAsUser` field. This field contains the user ID assigned to the pod. Then replace the `<USER ID>` placeholder in the Dockerfile file with the retrieved user ID.
