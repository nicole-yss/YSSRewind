
import Foundation
import Capacitor
import UIKit

@objc(InstagramStoryPlugin)
public class InstagramStoryPlugin: CAPPlugin {

  @objc func shareVideo(_ call: CAPPluginCall) {
    guard let srcType = call.getString("sourceType"),
          let source  = call.getString("source"),
          let appId   = call.getString("appId") else {
      call.reject("Missing sourceType/source/appId"); return
    }

    let contentURL = call.getString("contentUrl") ?? ""
    let topColor    = call.getString("topColor") ?? "#000000"
    let bottomColor = call.getString("bottomColor") ?? "#000000"

    guard let storiesURL = URL(string: "instagram-stories://share?source_application=\(appId)"),
          UIApplication.shared.canOpenURL(storiesURL) else {
      call.reject("Instagram Stories not available"); return
    }

    loadVideoData(srcType: srcType, source: source) { data, err in
      DispatchQueue.main.async {
        if let err = err { call.reject(err.localizedDescription); return }
        guard let data = data else { call.reject("No media data"); return }

        let items: [String: Any] = [
          "com.instagram.sharedSticker.backgroundVideo": data,
          "com.instagram.sharedSticker.contentURL": contentURL,
          "com.instagram.sharedSticker.topBackgroundColor": topColor,
          "com.instagram.sharedSticker.bottomBackgroundColor": bottomColor
        ]
        UIPasteboard.general.setItems([items], options: [.expirationDate: Date().addingTimeInterval(300)])
        UIApplication.shared.open(storiesURL, options: [:], completionHandler: nil)
        call.resolve()
      }
    }
  }

  private func loadVideoData(srcType: String, source: String, completion: @escaping (Data?, Error?) -> Void) {
    if srcType == "remoteUrl", let url = URL(string: source) {
      URLSession.shared.dataTask(with: url) { d,_,e in completion(d,e) }.resume()
    } else if srcType == "filePath" {
      let url = source.hasPrefix("file://") ? URL(string: source)! : URL(fileURLWithPath: source)
      do { completion(try Data(contentsOf: url), nil) } catch { completion(nil, error) }
    } else if srcType == "base64" {
      completion(Data(base64Encoded: source, options: .ignoreUnknownCharacters), nil)
    } else {
      completion(nil, NSError(domain: "yss.instagram", code: -1, userInfo: [NSLocalizedDescriptionKey: "Unsupported sourceType"]))
    }
  }
}
