<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=!messagesPerField.existsError('account', 'username','password') displayInfo=realm.password && realm.registrationAllowed && !registrationDisabled??; section>
    <#if section = "header">
        ${msg("loginAccountTitle")}
    <#elseif section = "form">
        <#if realm.password>
            <form id="kc-form-login" onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post">
                <div class="${properties.kcFormGroupClass!}">
                    <label for="account" class="${properties.kcLabelClass!}">${msg("account")}</label>
                    <input tabindex="1" id="account" class="${properties.kcInputClass!}" name="account" type="text" autocomplete="off" autofocus
                           aria-invalid="<#if messagesPerField.existsError('account', 'username','password')>true</#if>"
                    />
                </div>
                <div class="${properties.kcFormGroupClass!}">
                    <label for="username" class="${properties.kcLabelClass!}">${msg("usernameOrEmail")}</label>
                    <input tabindex="2" id="username" class="${properties.kcInputClass!}" name="username"  type="text" autocomplete="off"
                           aria-invalid="<#if messagesPerField.existsError('account', 'username','password')>true</#if>"
                    />
                </div>
                <div class="${properties.kcFormGroupClass!}">
                    <label for="password" class="${properties.kcLabelClass!}">${msg("password")}</label>
                    <input tabindex="3" id="password" class="${properties.kcInputClass!}" name="password" type="password" autocomplete="off"
                           aria-invalid="<#if messagesPerField.existsError('account', 'username','password')>true</#if>"
                    />
                </div>
                <div id="kc-form-buttons" class="${properties.kcFormGroupClass!}">
                    <input type="hidden" id="id-hidden-input" name="credentialId" <#if auth.selectedCredential?has_content>value="${auth.selectedCredential}"</#if>/>
                    <input tabindex="4" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" name="login" id="kc-login" type="submit" value="${msg("doLogIn")}"/>
                </div>
            </form>
        </#if>
    </#if>
</@layout.registrationLayout>