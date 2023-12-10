# ikazuchi : thing reserve system
いかづち：設備予約システム

## 実行
Spring Boot App を実行して、ブラウザで http://localhost:8080 にアクセスします。
```
$ ./gradlew bootRun
...
```
```
$ # win
$ start chrome http://localhost:8080
```

## 機能
システムの機能は次の通りです。

### ログイン(login)
システムにログインします。
アカウントのサンプルは次の通りです。
|名前|パス|ロール|
|--|--|:--:|
|ikazuchi|thunder|管理|

### ツールバー(toolbar)
メニューを表示します。
メニューから予約、設備、アカウントの画面に遷移できます。
また、システムからログアウトできます。

### 予約(reserve)
設備を予約、キャンセルできます。

### 設備(thing)
設備を追加、編集、削除できます。

### アカウント(account)
アカウントを追加、編集、削除できます。
