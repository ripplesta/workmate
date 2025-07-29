# WorkMate学習記録

## 1週目(7/21~7/27)  
###  Spting Bootを使ったプロジェクトの初作成
- まず初めに動作確認をしたがファイルを作成しようとしてエラーが発生  
  →　Servletとjspを使用するものだと思っていたが.javaとHTMLで作成するのだと理解した
- 実行時にエラー発生(DB接続の設定をしていなくてエラー)
  →application propertiesにDB接続情報を記述して解決
- サンプルファイルを作成してデータの受け渡しも問題なく実行できた

#### 学びメモ
- GetMapping("/hello")で/helloにアクセスするとそのメソッドが実行される  
  → return "hello";でtemplate/hello.htmlを表示する

### 「ログイン機能」実装記録
- 入力フォームからログインIDとパスワードを受け取る  
- Spring Bootの `@PostMapping` で受け取りDBと照合  
- 一致した場合はセッションを作成し、メニューページへ遷移  
- 一致しない場合は、エラーメッセージを赤い文字で表示させる

#### 学びメモ
- `@アノテーション` により、クラスやメソッドに役割や制約を指定できる
- @EntityはJavaクラスをDBテーブルに対応させる。@Tableでテーブル名をマッピングしたり@Columnでエンティティを定義して自動生成したりできる
- @Idで主キー指定や@GenerationType.IDENTITYで主キー値を自動で割り当てる
- Modelはコントローラーからビューへ値を渡すための入れ物(Mapのようなもの)
- addAttribute("キー", 値)で文字列やオブジェクトなどを渡せる
- html側は"${キー}"で受け取る  
  (例　model.addAttribute("title", "ログインページ") → <h1 th:text="${title}</h1> <!-- ログインページ -->
- HttpSession でセッション管理ができる(例：session.setAttribute("loginUser", account)でセッションに保存
- session.getAttribute("loginUser")でセッションから値を取得
  → Account loginUser = (Account)session.getAttribute("loginUser")など型変換が必要
- HTML側で使うときはth:text="${session.loginUser.userName}"とsession.を使って属性にアクセス
- session.invalidate()でセッション破棄
- return "redirect:/htmlファイル名"でそのページにリダイレクト

### 「新規ユーザー登録」実装記録
- 

